package com.sparta.outsourcing.service;

import com.sparta.outsourcing.dto.menu.MenuRequestDto;
import com.sparta.outsourcing.dto.menu.MenuResponseDto;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.repository.MenuRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;


    @Transactional
    public void create(Long storeId, MenuRequestDto requestDto) {
        Store findStore = storeRepository.findById(storeId).orElseThrow();
        Menu menu = new Menu(requestDto, findStore);
        Menu saveMenu = menuRepository.save(menu);
    }

    public List<MenuResponseDto> getMenus(Long storeId) {
        Store findStore = storeRepository.findById(storeId).orElseThrow(() -> new EntityNotFoundException("해당 가게가 없습니다."));
        List<Menu> menus = findStore.getMenus();
        return menus.stream()
                .map(MenuResponseDto::ofDto)
                .toList();
    }

    @Transactional
    public void update(Long menuId, MenuRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("해당 메뉴가 없습니다."));
        menu.update(requestDto);
    }

    @Transactional
    public void delete(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("해당 메뉴가 없습니다."));
        menuRepository.delete(menu);
    }
}
