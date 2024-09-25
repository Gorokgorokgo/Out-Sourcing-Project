package com.sparta.outsourcing.service;

import com.sparta.outsourcing.constant.MenuStatus;
import com.sparta.outsourcing.constant.UserRoleEnum;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.menu.MenuResponseDto;
import com.sparta.outsourcing.entity.*;
import com.sparta.outsourcing.repository.FileRepository;
import com.sparta.outsourcing.dto.store.StoreRequestDto;
import com.sparta.outsourcing.dto.store.StoreResponseDto;
import com.sparta.outsourcing.dto.store.StoreStatusUpdateDto;
import com.sparta.outsourcing.dto.store.StoreUpdateRequestDto;
import com.sparta.outsourcing.exception.common.UnauthorizedAccessException;
import com.sparta.outsourcing.exception.customer.CustomerNotFoundException;
import com.sparta.outsourcing.exception.file.ImageUploadLimitExceededException;
import com.sparta.outsourcing.exception.store.MaxStoreLimitReachedException;
import com.sparta.outsourcing.exception.store.StoreNotFoundException;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.MenuRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;

    private final MenuRepository menuRepository;

    // 가게 생성
    @Transactional
    public StoreResponseDto createStore(AuthUser authUser, StoreRequestDto requestDto, List<MultipartFile> files) throws IOException {
        Customer findCustomer = getFindCustomer(authUser);  // 사용자 확인

        checkAdminAuthority(authUser); // ADMIN 인지 확인
        checkStoreLimit(authUser);  // 가게 수 확인

        Store newStore = storeRepository.save(new Store(findCustomer, requestDto));


        if (files != null && !files.isEmpty()) {
            if (files.size() > 1)
                throw new ImageUploadLimitExceededException("파일은 1개만 업로드 가능합니다.");
            fileService.uploadFiles(newStore.getStoreId(), files, ImageEnum.STORE);
        }

        List<Image> byItemIdAndImageEnum = fileRepository.findByItemIdAndImageEnum(newStore.getStoreId(), ImageEnum.STORE);
        StoreResponseDto storeResponseDto = new StoreResponseDto(newStore);
        storeResponseDto.setImage(byItemIdAndImageEnum);
        return storeResponseDto;
    }

    // 가게 단건 조회
    public StoreResponseDto getStore(Long storeId) {
        List<MenuStatus> statuses = Arrays.asList(MenuStatus.ACTIVE, MenuStatus.OUT_OF_STOCK);
        Store findStore = getStore(storeId, statuses);  // 상태 만족하는 가게 & 메뉴 조회

        // 가게 이미지 조회
        List<Image> storeImages = fileRepository.findByItemIdAndImageEnum(findStore.getStoreId(), ImageEnum.STORE);
        StoreResponseDto storeResponseDto = new StoreResponseDto(findStore);
        storeResponseDto.setImage(storeImages);

        // 메뉴 이미지와 함께 처리
        List<MenuResponseDto> menus = findStore.getMenus().stream()
                .filter(menu -> statuses.contains(menu.getMenuStatus()))
                .map(menu -> {
                    // 각 메뉴에 대해 이미지 조회
                    List<Image> menuImages = fileRepository.findByItemIdAndImageEnum(menu.getMenuId(), ImageEnum.MENU);
                    MenuResponseDto menuResponseDto = new MenuResponseDto(menu);
                    menuResponseDto.setImage(menuImages); // 메뉴 이미지 설정
                    return menuResponseDto;
                })
                .collect(Collectors.toList());

        storeResponseDto.setMenus2(menus);

        return storeResponseDto;
    }

    // 가게 다건 조회
    @Transactional
    public Page<StoreResponseDto> getStores(Pageable pageable) {
        Page<Store> stores = storeRepository.findAllByStoreStatus(true, pageable);  // 개업 상태 가게 목록 조회
        return stores.map(store -> {
            // Store 이미지 처리
            List<Image> storeImages = fileRepository.findByItemIdAndImageEnum(store.getStoreId(), ImageEnum.STORE);
            StoreResponseDto storeResponseDto = new StoreResponseDto(store);
            storeResponseDto.setImage(storeImages);

            // 메뉴 처리
            List<MenuResponseDto> menus = store.getMenus().stream().map(menu -> {
                // 메뉴 이미지 처리
                List<Image> menuImages = fileRepository.findByItemIdAndImageEnum(menu.getMenuId(), ImageEnum.MENU);
                MenuResponseDto menuResponseDto = new MenuResponseDto(menu);
                menuResponseDto.setImage(menuImages);  // 메뉴 이미지 설정
                return menuResponseDto;
            }).collect(Collectors.toList());

            storeResponseDto.setMenus2(menus);
            return storeResponseDto;
        });
    }


    // 가게 수정
    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, Long storeId, StoreUpdateRequestDto requestDto, List<MultipartFile> file) throws IOException {
        Customer findCustomer = getFindCustomer(authUser);  // 사용자 확인
        Store findStore = getFindStore(storeId);    // 수정할 가게 확인

        checkAdminAuthority(authUser); // ADMIN 인지 확인
        checkStoreOwnership(authUser, findStore);  // 본인 가게인지 확인


        if (file != null && !file.isEmpty()) {
            if (file.size() > 1)
                throw new ImageUploadLimitExceededException("파일은 1개만 업로드 가능합니다.");
            fileRepository.findByItemIdAndImageEnum(findStore.getStoreId(), ImageEnum.STORE).forEach(fileRepository::delete);
            fileService.uploadFiles(findStore.getStoreId(), file, ImageEnum.STORE);
        }
        findStore.update(findCustomer, requestDto);

        StoreResponseDto storeResponseDto = new StoreResponseDto(findStore);
        storeResponseDto.setImage(fileRepository.findByItemIdAndImageEnum(findStore.getStoreId(), ImageEnum.STORE));



        return storeResponseDto;
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
        if (!authUser.getRole().getAuthority().equals(UserRoleEnum.OWNER.getAuthority())) {
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
