package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.menu.MenuRequestDto;
import com.sparta.outsourcing.dto.menu.MenuResponseDto;
import com.sparta.outsourcing.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private MenuService menuService;

    @PostMapping("/menus")
    public ResponseEntity<String> create(@PathVariable Long storeId, @RequestBody MenuRequestDto requestDto) {
        menuService.create(storeId, requestDto);
        return ResponseEntity.ok().body("메뉴가 생성되었습니다.");
    }

    @GetMapping("/menus")
    public ResponseEntity<List<MenuResponseDto>> getMenus(@PathVariable Long storeId) {
        return ResponseEntity.ok(menuService.getMenus(storeId));
    }

    @PutMapping("/menus/{menuId}")
    public ResponseEntity<String> update(@PathVariable Long menuId, @RequestBody MenuRequestDto requestDto) {
        menuService.update(menuId, requestDto);
        return ResponseEntity.ok().body("메뉴가 수정되었습니다.");
    }

    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<String> delete(@PathVariable Long menuId) {
        menuService.delete(menuId);
        return ResponseEntity.ok().body("메뉴가 삭제되었습니다.");
    }
}
