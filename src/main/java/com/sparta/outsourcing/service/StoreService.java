package com.sparta.outsourcing.service;

import com.sparta.outsourcing.constant.UserRoleEnum;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.store.StoreRequestDto;
import com.sparta.outsourcing.dto.store.StoreResponseDto;
import com.sparta.outsourcing.dto.store.StoreStatusUpdateDto;
import com.sparta.outsourcing.dto.store.StoreUpdateRequestDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.exception.store.*;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        checkAdminAuthority(authUser);  // 사용자 권한 확인
        checkStoreLimit(authUser);  // 가게 수 확인

        Store newStore = storeRepository.save(new Store(findCustomer, requestDto));
        return new StoreResponseDto(newStore);
    }

    // 가게 단건 조회
    public StoreResponseDto getStore(Long storeId) {
        Store findStore = getFindStore(storeId);    // 조회 할 가게 확인

        validateStoreIsOpen(findStore); // 폐업상태 가게 조회 시 예외

        return new StoreResponseDto(findStore);
    }

    // 가게 다건 조회
    @Transactional
    public Page<StoreResponseDto> getStores(Pageable pageable) {
        Page<Store> stores = storeRepository.findAllByStoreStatus(true, pageable);  // 개업상태 가게목록 조회
        return stores.map(StoreResponseDto::new);
    }

    // 가게 정보 업데이트
    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, Long storeId, StoreUpdateRequestDto requestDto) {
        Customer findCustomer = getFindCustomer(authUser);  // 사용자 확인
        Store findStore = getFindStore(storeId);    // 수정할 가게 확인

        checkAdminAuthority(authUser);  // 사용자 권한 확인
        findStore.update(findCustomer, requestDto);
        return new StoreResponseDto(findStore);
    }

    // 가게 상태 변경
    @Transactional
    public StoreStatusUpdateDto updateStoreStatus(AuthUser authUser, Long storeId, boolean storeStatus) {
        Store findStore = getFindStore(storeId);

        checkAdminAuthority(authUser);

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
            throw new UnauthorizedAccessException("사장님만 가게를 생성할 수 있습니다.");
        }
    }

    private void checkStoreLimit(AuthUser authUser) {
        if (storeRepository.countByCustomer_CustomerIdAndStoreStatus(authUser.getCustomerId(), true) >= 3) {
            throw new MaxStoreLimitReachedException("가게는 최대 3개까지만 운영 가능합니다.");
        }
    }

    private void validateStoreIsOpen(Store store) {
        if (!store.isStoreStatus()) {
            throw new StoreClosedException("폐업 상태의 가게는 조회할 수 없습니다.");
        }
    }
}
