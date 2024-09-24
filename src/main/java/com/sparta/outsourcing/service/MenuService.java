package com.sparta.outsourcing.service;

import com.sparta.outsourcing.constant.MenuStatus;
import com.sparta.outsourcing.constant.UserRoleEnum;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.menu.MenuRequestDto;
import com.sparta.outsourcing.dto.menu.MenuResponseDto;
import com.sparta.outsourcing.dto.menu.MenuStatusUpdateDto;
import com.sparta.outsourcing.dto.menu.MenuUpdateDto;
import com.sparta.outsourcing.entity.*;
import com.sparta.outsourcing.exception.file.ImageUploadLimitExceededException;
import com.sparta.outsourcing.exception.menu.MenuNotFoundException;
import com.sparta.outsourcing.exception.customer.CustomerNotFoundException;
import com.sparta.outsourcing.exception.store.StoreNotFoundException;
import com.sparta.outsourcing.exception.common.UnauthorizedAccessException;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.FileRepository;
import com.sparta.outsourcing.repository.MenuRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;

    private final FileService fileService;
    private final FileRepository fileRepository;

    // 메뉴 생성
    @Transactional
    public MenuResponseDto createMenu(AuthUser authUser, Long storeId, MenuRequestDto requestDto, List<MultipartFile> files) throws IOException {
        Customer findCustomer = getFindCustomer(authUser);  // 사용자 확인
        Store findStore = getFindStore(storeId);    // 가게 있는지 확인

        checkAdminAuthority(authUser);  // ADMIN 권한 확인
        checkStoreOwnership(authUser, findStore);  // 본인 가게인지 확인

        if (files != null && !files.isEmpty()) {
            if (files.size() > 1)
                throw new ImageUploadLimitExceededException("파일은 1개만 업로드 가능합니다.");
            fileService.uploadFiles(findStore.getStoreId(), files, ImageEnum.MENU);
        }
        List<Image> byItemIdAndImageEnum = fileRepository.findByItemIdAndImageEnum(findStore.getStoreId(), ImageEnum.MENU);
        Menu menu = new Menu(findCustomer, findStore, requestDto);
        MenuResponseDto menuResponseDto = new MenuResponseDto(menuRepository.save(menu));
        menuResponseDto.setImage(byItemIdAndImageEnum);

        return menuResponseDto;
    }

    // 메뉴 수정
    @Transactional
    public MenuResponseDto updateMenu(AuthUser authUser, Long storeId, Long menuId, MenuUpdateDto requestDto) {
        Customer findCustomer = getFindCustomer(authUser);  // 사용자 확인
        Store findStore = getFindStore(storeId);    // 수정 할 가게 있는지 확인
        Menu findMenu = getFindMenu(menuId);    // 수정 할 메뉴 확인

        checkAdminAuthority(authUser);  // ADMIN 권한 확인
        checkStoreOwnership(authUser, findStore);  // 본인 가게인지 확인

        findMenu.update(findCustomer, findStore, requestDto);
        return new MenuResponseDto(findMenu);
    }

    // 메뉴 상태 변경
    @Transactional
    public MenuStatusUpdateDto updateMenuStatus(AuthUser authUser, Long storeId, Long menuId, MenuStatusUpdateDto statusUpdateDto) {
        getFindCustomer(authUser);  // 사용자 확인
        Store findStore = getFindStore(storeId);// 수정 할 가게 확인
        Menu findMenu = getFindMenu(menuId);    // 수정 할 메뉴 확인

        checkAdminAuthority(authUser);  // ADMIN 권한 확인
        checkStoreOwnership(authUser, findStore);  // 본인 가게인지 확인

        MenuStatus newStatus = statusUpdateDto.getMenuStatus(); // Enum 타입으로 직접 가져오기

        // 상태 업데이트
        switch (newStatus) {
            case OUT_OF_STOCK:  // 품절
                findMenu.setOutOfStock();
                break;
            case DELETED:   // 삭제
                findMenu.markAsDeleted();
                break;
            case ACTIVE:    // 판매중
                findMenu.setActive();
                break;
            default:
                throw new IllegalArgumentException("Invalid menu status"); // 유효하지 않은 상태에 대한 예외 처리
        }

        return new MenuStatusUpdateDto(findMenu);
    }

    private Customer getFindCustomer(AuthUser authUser) {
        return customerRepository.findById(authUser.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private Store getFindStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));
    }

    private Menu getFindMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
    }

    private void checkAdminAuthority(AuthUser authUser) {
        if(!authUser.getRole().getAuthority().equals(UserRoleEnum.OWNER.getAuthority())) {
            throw new UnauthorizedAccessException("사장님만 가능합니다.");
        }
    }

    private void checkStoreOwnership(AuthUser authUser, Store store) {
        if (!store.getCustomer().getCustomerId().equals(authUser.getCustomerId())) {
            throw new UnauthorizedAccessException("본인의 가게만 작업할 수 있습니다.");
        }
    }
}
