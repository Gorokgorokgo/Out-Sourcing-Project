package com.sparta.outsourcing.service;

import com.sparta.outsourcing.dto.store.StoreResponseDto;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.repository.MenuRepository;
import com.sparta.outsourcing.repository.SearchRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchRepository searchRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

//    public List<StoreResponseDto> searchAll(String keyword, String address) {
//        //가게명이 들어간 단어 찾기
//        String storeKeyword = "[가-힣]*\\s*"+keyword+"+\\s*[가-힣]*";
//        //가게 이름이 일치하는 경우 찾기
//        List<Store> storeList = storeRepository.findAllByNameQuery(storeKeyword);
//        //결과가 15개 이상이면 전달
//        if(storeList.size() > 15) return storeList.stream().map(StoreResponseDto::new).toList();
//
//        //주소 앞부분 찾기
//        String[] addressCut= address.split(" ");
//        String addressDong = addressCut[0]+" "+addressCut[1]+" "+addressCut[2];
//        //메뉴 이름에 keword가 들어가 있고 가게의 dong까지 같은 store 찾기
//        //메뉴 이름에 keword가 들어간 메뉴 찾기
//        List<Menu> menuList = menuRepository.findAllByNameLike(keyword);
//        //각 메뉴를
//        storeList.addAll(menuList.stream().map(m->m.getStore()).toList());
//        return storeList.stream().map(StoreResponseDto::new).toList();
//    }
}
