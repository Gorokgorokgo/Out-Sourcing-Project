package com.sparta.outsourcing.service;

import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;

  public Menu findById(Long menuId) {
    return menuRepository.findById(menuId).orElseThrow(
        () -> new RuntimeException("메뉴를 찾을 수 없습니다."));
  }
}
