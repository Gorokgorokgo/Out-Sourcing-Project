package com.sparta.outsourcing.service;

import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.search.SearchAllResponseDto;
import com.sparta.outsourcing.dto.search.SearchLatestResponseDto;
import com.sparta.outsourcing.dto.search.SearchRequestDto;
import com.sparta.outsourcing.dto.search.SearchResponsDto;
import com.sparta.outsourcing.dto.store.StoreDetailResponseDto;
import com.sparta.outsourcing.dto.store.StoreSimpleResponseDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.Search;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.exception.common.WrongInputException;
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
    private final CommonService commonService;
    //공차 -> 공차 쌍문역점, 공차 수유역점 공차리얼
    //빽다 -> 메뉴중에 빽다가 들어간 메뉴가 검색되도록
    //공차리얼

    public SearchAllResponseDto searchAll(String keyword, String address) {

        if(keyword == null) throw new WrongInputException("keyword가 입력되지 않았습니다.");
        if(address == null || address.split(" ").length < 3) throw new WrongInputException("address를 다시 입력해주세요.");
        //가게 검색 결과
        List<StoreSimpleResponseDto> storeResult = new ArrayList<>();
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
        List<Store> storeList = storeRepository.findAllByStoreNameContains(keyword);
        //store중 개업한 경우만 확인
        storeList = storeList.stream().filter(store -> store.isStoreStatus()==true).toList();
        //가게가 없으면 검색된 가게 결과 없다고 오류
        if(storeList.isEmpty()) log.info("통합검색 :: 가게 검색 결과 없음");

        //검색 결과 중 같은 동에 있는 것을 위주로 확인
        List<Store> storeDongList = storeList.stream().filter(store -> store.getAddress().contains(addressDong)).toList();
        storeResult.addAll(storeDongList.stream().map(StoreSimpleResponseDto::new).toList());
        //결과가 4개 이하이면 구까지 넓혀서 검색
        if(storeResult.size() < 5) {
            //같은 구에 있는 가게 찾기(앞의 중복되는 가게는 지우기)
            List<Store> storeGuList = getGuStore (storeList, addressGu,storeDongList);
            storeResult.addAll(storeGuList.stream().map(StoreSimpleResponseDto::new).toList());
            //그래도 검색 결과가 너무 적으면 전체로 검색
            if(storeResult.size() < 5) {
                //전체 중에 앞의 중복되는 가게를 제외한 검색 결과
                List<Store> storeLastList = getAllStore (storeList, storeGuList,storeDongList);
                storeResult.addAll(storeLastList.stream().map(StoreSimpleResponseDto::new).toList());
            }
        }
        //검색 결과를 5개만 출력
        if(storeResult.size() > 5) storeResult = new ArrayList<>(storeResult.subList(0, 5));


        //메뉴 검색
        //메뉴 이름에 keword가 들어간 menu찾기
        List<Menu> menuList = menuRepository.findAllByMenuNameContains(keyword);
        //가게가 개업한 곳만 확인
        menuList = menuList.stream().filter(menu -> menu.getStore().isStoreStatus()==true).toList();
        //검색 결과 없으면 없다고 말함
        if(menuList.isEmpty()) log.info("통합검색 :: 메뉴 검색 결과 없음");

        //같은 동에 있는 메뉴를 먼저 설정
        List<Menu> menuDongList = menuList.stream().filter(menu -> menu.getStore().getAddress().contains(addressDong)).toList();
        storeMenuList= menuDongList.stream().map(Menu::getStore).toList();
        menuListAdd(menuResult, storeMenuList, menuDongList);
        //검색 결과가 너무 적으면 구로 넓혀서 검색
        if(menuResult.size() < 5) {
            //가게가 같은 구에 위차한 메뉴 찾기(앞과 중복되는 결과는 제외)
            List<Menu> menuGuList = getGuMenu(menuList, addressGu, menuDongList);
            //각 메뉴를 가게로 바꾸기
            storeMenuList = menuGuList.stream().map(Menu::getStore).toList();
            //결과값을 responseDto형태로 바꿈
            menuListAdd(menuResult, storeMenuList, menuGuList);
            //그래도 너무 적으면 전체 검색 (앞과 중복되는 결과는 제외)
            if(menuResult.size() < 5) {
                List<Menu> menuLastList = getAllMenu(menuList, menuGuList, menuDongList);
                storeMenuList = menuLastList.stream().map(Menu::getStore).toList();
                menuListAdd(menuResult, storeMenuList, menuLastList);
            }
        }
        //검색 결과를 5개만 출력
        if(menuResult.size() > 5) menuResult = new ArrayList<>(menuResult.subList(0, 5));
        return new SearchAllResponseDto(storeResult, menuResult);
    }

    @Transactional
    public SearchResponsDto createSearch(AuthUser authUser, SearchRequestDto requestDto) {
        Customer customer = commonService.findCustomerById(authUser.getCustomerId());
        Search search = new Search(customer, requestDto);
        Search savedSearch = searchRepository.save(search);
        return new SearchResponsDto(savedSearch);
    }

    public SearchLatestResponseDto searchLatest(AuthUser authUser) {
        Customer customer = commonService.findCustomerById(authUser.getCustomerId());
        List<Search> searchResult = searchRepository.findAllByCustomerCustomerIdOrderByCreatedAtDesc(authUser.getCustomerId());
        if (searchResult.isEmpty()) log.info(":::최근 검색 결과 없음");
        return new SearchLatestResponseDto(customer, searchResult);
    }

    private void menuListAdd (List<StoreDetailResponseDto> menuResult, List<Store> storeMenuList, List<Menu> menuList) {
        for(int i = 0; i < storeMenuList.size(); i++) {
            menuResult.add(new StoreDetailResponseDto(menuList.get(i), storeMenuList.get(i)));
        }
    }

    private List<Store> getGuStore (List<Store> storeList, String addressGu,List<Store> storeDongList) {
        return storeList.stream()
                .filter(store -> store.getAddress().contains(addressGu))
                .filter(store -> !storeDongList.contains(store))
                .toList();
    }

    private List<Store> getAllStore (List<Store> storeList, List<Store> storeGuList,List<Store> storeDongList) {
        return storeList.stream()
                .filter(store -> !storeDongList.contains(store))
                .filter(store -> !storeGuList.contains(store))
                .toList();
    }

    private List<Menu> getGuMenu(List<Menu> menuList, String addressGu, List<Menu> menuDongList) {
        return menuList.stream()
                .filter(menu -> menu.getStore().getAddress().contains(addressGu))
                .filter(menu -> !menuDongList.contains(menu))
                .toList();
    }

    private List<Menu> getAllMenu(List<Menu> menuList, List<Menu> menuGuList, List<Menu> menuDongList) {
        return menuList.stream()
                .filter(menu -> !menuDongList.contains(menu))
                .filter(menu -> !menuGuList.contains(menu))
                .toList();
    }
}
