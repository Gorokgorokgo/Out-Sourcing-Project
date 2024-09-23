package com.sparta.outsourcing.service;

import com.sparta.outsourcing.constant.MenuStatus;
import com.sparta.outsourcing.constant.UserRoleEnum;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.store.StoreRequestDto;
import com.sparta.outsourcing.dto.store.StoreResponseDto;
import com.sparta.outsourcing.dto.store.StoreStatusUpdateDto;
import com.sparta.outsourcing.dto.store.StoreUpdateRequestDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.exception.common.UnauthorizedAccessException;
import com.sparta.outsourcing.exception.customer.CustomerNotFoundException;
import com.sparta.outsourcing.exception.store.MaxStoreLimitReachedException;
import com.sparta.outsourcing.exception.store.StoreNotFoundException;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;

    // 가게 생성
    @Transactional
    public StoreResponseDto createStore(AuthUser authUser, StoreRequestDto requestDto) {
        Customer findCustomer = getFindCustomer(authUser);  // 사용자 확인

        checkAdminAuthority(authUser); // ADMIN 인지 확인
        checkStoreLimit(authUser);  // 가게 수 확인

        Store newStore = storeRepository.save(new Store(findCustomer, requestDto));
        return new StoreResponseDto(newStore);
    }

    // 가게 단건 조회
    public StoreResponseDto getStore(Long storeId) {
        List<MenuStatus> statuses = Arrays.asList(MenuStatus.ACTIVE, MenuStatus.OUT_OF_STOCK);
        Store findStore = getStore(storeId, statuses);  // 상태 만족하는 가게 & 메뉴 조회

        StoreResponseDto storeResponseDto = new StoreResponseDto(findStore);
        storeResponseDto.setMenus(findStore.getMenus().stream()
                    .filter(menu -> statuses.contains(menu.getMenuStatus()))
                    .collect(Collectors.toList()));

        return storeResponseDto;
    }

    // 가게 다건 조회
    @Transactional
    public Page<StoreResponseDto> getStores(Pageable pageable) {
        Page<Store> stores = storeRepository.findAllByStoreStatus(true, pageable);  // 개업상태 가게목록 조회
        return stores.map(StoreResponseDto::new);
    }

    // 가게 수정
    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, Long storeId, StoreUpdateRequestDto requestDto) {
        Customer findCustomer = getFindCustomer(authUser);  // 사용자 확인
        Store findStore = getFindStore(storeId);    // 수정할 가게 확인

        checkAdminAuthority(authUser); // ADMIN 인지 확인
        checkStoreOwnership(authUser, findStore);  // 본인 가게인지 확인

        findStore.update(findCustomer, requestDto);
        return new StoreResponseDto(findStore);
    }

    // 가게 상태 변경
    @Transactional
    public StoreStatusUpdateDto updateStoreStatus(AuthUser authUser, Long storeId, boolean storeStatus) {
        Store findStore = getFindStore(storeId);    // 수정할 가게 확인

        checkAdminAuthority(authUser); // ADMIN 인지 확인
        checkStoreOwnership(authUser, findStore);  // 본인 가게인지 확인

        // storeStatus 값에 따라 가게 상태를 개업(true) 또는 폐업(false)으로 변경
        if (storeStatus) {
            findStore.openStore();
        } else {
            findStore.closeStore();
        }
        return new StoreStatusUpdateDto(findStore);
    }

    private Customer getFindCustomer(AuthUser authUser) {
        return customerRepository.findById(authUser.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private Store getFindStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));
    }

    private void checkAdminAuthority(AuthUser authUser) {
        if(!authUser.getRole().getAuthority().equals(UserRoleEnum.ADMIN.getAuthority())) {
            throw new UnauthorizedAccessException("사장님만 가능합니다.");
        }
    }

    private void checkStoreOwnership(AuthUser authUser, Store store) {
        if (!store.getCustomer().getCustomerId().equals(authUser.getCustomerId())) {
            throw new UnauthorizedAccessException("본인의 가게만 작업할 수 있습니다.");
        }
    }

    private void checkStoreLimit(AuthUser authUser) {
        if (storeRepository.countByCustomer_CustomerIdAndStoreStatus(authUser.getCustomerId(), true) >= 3) {
            throw new MaxStoreLimitReachedException("가게는 최대 3개까지만 운영 가능합니다.");
        }
    }

    private Store getStore(Long storeId, List<MenuStatus> statuses) {
        return storeRepository.findByIdAndStoreStatusAndMenuStatusIn(storeId, statuses)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));
    }
}
