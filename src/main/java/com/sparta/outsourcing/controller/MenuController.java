package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.menu.MenuRequestDto;
import com.sparta.outsourcing.dto.menu.MenuResponseDto;
import com.sparta.outsourcing.dto.menu.MenuStatusUpdateDto;
import com.sparta.outsourcing.dto.menu.MenuUpdateDto;
import com.sparta.outsourcing.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 생성
    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<MenuResponseDto> createMenu(@Auth AuthUser authUser,
                                                      @PathVariable Long storeId,
                                                      @RequestPart MenuRequestDto requestDto,
                                                      @RequestPart(value = "file", required = false) List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(menuService.createMenu(authUser, storeId, requestDto, files));
    }

    // 메뉴 수정
    @PutMapping("/stores/{storeId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@Auth AuthUser authUser,
                                                      @PathVariable Long storeId,
                                                      @PathVariable Long menuId,
                                                      @RequestPart MenuUpdateDto requestDto,
                                                      @RequestPart(value = "file", required = false) List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(menuService.updateMenu(authUser, storeId, menuId, requestDto, files));
    }

    // 메뉴 상태 변경
    @PutMapping("/stores/{storeId}/menus/{menuId}/status")
    public ResponseEntity<MenuStatusUpdateDto> updateMenuStatus(@Auth AuthUser authUser,
                                                                @PathVariable Long storeId,
                                                                @PathVariable Long menuId,
                                                                @RequestBody MenuStatusUpdateDto statusUpdateDto) {
        return ResponseEntity.ok(menuService.updateMenuStatus(authUser, storeId, menuId, statusUpdateDto));
    }
}
