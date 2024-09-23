package com.sparta.outsourcing.service;

import com.sparta.outsourcing.dto.SearchResponseDto;
import com.sparta.outsourcing.dto.store.StoreDetailResponseDto;
import com.sparta.outsourcing.dto.store.StoreResponseDto;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.repository.MenuRepository;
import com.sparta.outsourcing.repository.SearchRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchRepository searchRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    //공차 -> 공차 쌍문역점, 공차 수유역점 공차리얼
    //빽다 -> 메뉴중에 빽다가 들어간 메뉴가 검색되도록
    //공차리얼

    public SearchResponseDto searchAll(String keyword, String address) {
        //가게 검색 결과
        List<StoreResponseDto> storeResult = new ArrayList<>();
        //메뉴 검색 결과
        List<StoreDetailResponseDto> menuResult = new ArrayList<>();
        //메뉴 -> 가게 리스트
        List<Store> storeMenuList;

        //주소 자르고 동과 구로 주소 만듬
        String[] addressCut= address.split(" ");
        String addressDong = addressCut[0]+" "+addressCut[1]+" "+addressCut[2];
        String addressGu = addressCut[0]+" "+addressCut[1];
        //가게 검색
        //가게 이름이 일치하는 경우 찾기
        List<Store> storeList = storeRepository.findAllByNameContains(keyword);
        //가게가 없으면 검색된 가게 결과 없다고 오류
        if(storeList.isEmpty()) log.info("통합검색 :: 가게 검색 결과 없음");
        //결과가 15개 이상이면 같은 동에 있는 것만 전달
        if(storeList.size() > 4) {
            List<Store> storeDongList = storeList.stream().filter(store -> store.getAddress().contains(addressDong)).toList();
            storeResult.addAll(storeDongList.stream().map(StoreResponseDto::new).toList());
            //검색 결과가 너무 적으면 구까지 넓혀서 검색
            if(storeDongList.size() < 2) {
                List<Store> storeGuList = storeList.stream()
                        .filter(store -> store.getAddress().contains(addressGu))
                        .filter(store -> !storeDongList.contains(store))
                        .toList();
                storeResult.addAll(storeGuList.stream().map(StoreResponseDto::new).toList());
            }
        }
        else  storeResult = storeList.stream().map(StoreResponseDto::new).toList();

        //주소 앞부분 찾기
        //메뉴 이름에 keword가 들어간 menu찾기
        List<Menu> menuList = menuRepository.findAllByNameContains(keyword);
        //검색 결과 없으면 없다고 말함
        if(menuList.isEmpty()) log.info("통합검색 :: 메뉴 검색 결과 없음");

        //각 메뉴를 가게의 객체로 변환하여 storeList에 합치기
//        storeList.addAll(menuList.stream().map(m->m.getStore()).toList());
        //검색 결과가 15개 넘으면 가게가 동에 있는걸로 한정
        if(menuList.size() > 4) {
            List<Menu> menuDongList = menuList.stream().filter(menu -> menu.getStore().getAddress().contains(addressDong)).toList();
            storeMenuList= menuDongList.stream().map(menu -> menu.getStore()).toList();
            for(int i = 0; i < storeMenuList.size(); i++) {
                menuResult.add(new StoreDetailResponseDto(menuDongList.get(i), storeMenuList.get(i)));
            }
            //너무 적으면 가게가 구로 있는 데이터까지 확장
            if(menuDongList.size() < 2) {
                List<Menu> menuGuList = menuList.stream()
                        .filter(menu -> menu.getStore().getAddress().contains(addressGu))
                        .filter(menu -> !menuDongList.contains(menu))
                        .toList();
                storeMenuList = menuGuList.stream().map(m->m.getStore()).toList();
                for(int i = 0; i < storeMenuList.size(); i++) {
                    menuResult.add(new StoreDetailResponseDto(menuGuList.get(i), storeMenuList.get(i)));
                }
            }
            return new SearchResponseDto(storeResult, menuResult);
        }

        storeMenuList = menuList.stream().map(m->m.getStore()).toList();
        for(int i = 0; i < storeMenuList.size(); i++) {
            menuResult.add(new StoreDetailResponseDto(menuList.get(i), storeMenuList.get(i)));
        }
        return new SearchResponseDto(storeResult, menuResult);
    }
}
