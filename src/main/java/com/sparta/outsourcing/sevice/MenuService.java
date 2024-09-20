package com.sparta.outsourcing.sevice;

import com.sparta.outsourcing.dto.MenuRequestDto;
import com.sparta.outsourcing.dto.MenuResponseDto;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.repository.MenuRepository;
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


    @Transactional
    public MenuResponseDto create(MenuRequestDto requestDto) {
        Menu saveMenu = menuRepository.save(new Menu(requestDto));
        return new MenuResponseDto(saveMenu);
    }

//    public List<MenuResponseDto> getMenus(Long storeId) {
//        List<Menu> menus = menuRepository.findAllByStoreId(storeId);
//
//    }
}
