package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.dto.MenuRequestDto;
import com.sparta.outsourcing.dto.MenuResponseDto;
import com.sparta.outsourcing.sevice.MenuService;
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
    public ResponseEntity<MenuResponseDto> create(@RequestBody MenuRequestDto requestDto) {
        menuService.create(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @GetMapping("/menus")
//    public ResponseEntity<List<MenuResponseDto>> getMenus(@PathVariable Long storeId) {
//        menuService.getMenus(storeId);
//        return new ResponseEntity.ok()
//    }
}
