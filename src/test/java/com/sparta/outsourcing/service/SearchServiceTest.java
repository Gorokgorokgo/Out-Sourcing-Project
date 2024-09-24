package com.sparta.outsourcing.service;

import com.sparta.outsourcing.dto.search.SearchAllResponseDto;
import com.sparta.outsourcing.entity.Menu;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.exception.common.WrongInputException;
import com.sparta.outsourcing.repository.MenuRepository;
import com.sparta.outsourcing.repository.SearchRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @Mock
    SearchRepository searchRepository;

    @Mock
    StoreRepository storeRepository;

    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    SearchService searchService;

    private Store store1;


    private Menu menu1;
    private Menu menu2;
    private Menu menu3;

    private List<Store> stores;
    private List<Menu> menus;


    @BeforeEach
    void setUp() {
        store1 = spy(Store.class);
        ReflectionTestUtils.setField(store1, "storeId", 1L );
        ReflectionTestUtils.setField(store1, "storeName", "공차 미아뉴타운점");
        ReflectionTestUtils.setField(store1, "address", "서울 강북구 미아동 812");

        menu3 = spy(Menu.class);
        ReflectionTestUtils.setField(menu3, "menuId", 3L );
        ReflectionTestUtils.setField(menu3, "menuName", "공차 특별차");
        ReflectionTestUtils.setField(menu3, "store", store1);

    }

    @Nested
    class SearchAllTest {
        @Test
        void 통합검색_성공 () {
            //given
            String keyword = "공차";
            String address = "서울 강북구 수유동 11";
            given(storeRepository.findAllByStoreNameContains(keyword)).willReturn(List.of(store1));
            given(menuRepository.findAllByMenuNameContains(keyword)).willReturn(List.of(menu3));

            //when
            SearchAllResponseDto result = searchService.searchAll(keyword, address);

            //then
            assertEquals(1, result.getStoreList().size());
            assertEquals(1, result.getStoreMenuList().size());
            assertEquals("공차", result.getStoreMenuList().get(0).getMenu().getMenuName().split(" ")[0]);

        }

        @Test
        void 통합검색_가게_없음() {
            //given
            String keyword = "공차";
            String address = "서울 강북구 수유동 11";
            given(storeRepository.findAllByStoreNameContains(keyword)).willReturn(new ArrayList<>());
            given(menuRepository.findAllByMenuNameContains(keyword)).willReturn(List.of(menu3));
            //when
            SearchAllResponseDto result = searchService.searchAll(keyword, address);
            //then
            assertEquals(0, result.getStoreList().size());
            assertEquals(1, result.getStoreMenuList().size());
        }

        @Test
        void 통합검색_메뉴_없음() {
            //given
            String keyword = "공차";
            String address = "서울 강북구 수유동 11";
            given(storeRepository.findAllByStoreNameContains(keyword)).willReturn(List.of(store1));
            given(menuRepository.findAllByMenuNameContains(keyword)).willReturn(new ArrayList<>());
            //when
            SearchAllResponseDto result = searchService.searchAll(keyword, address);
            //then
            assertEquals(1, result.getStoreList().size());
            assertEquals(0, result.getStoreMenuList().size());
        }

        @Test
        void 통합검색_키워드_없음() {
            //given
            String keyword = null;
            String address = "서울 강북구 수유동 11";

            //when
            WrongInputException exception = assertThrows(WrongInputException.class, ()->searchService.searchAll(keyword, address));

            //then
            assertEquals("keyword가 입력되지 않았습니다.", exception.getMessage());
        }

        @Test
        void 통합검색_주소_없음() {
            //given
            String keyword = "공차";
            String address = null;

            //when
            WrongInputException exception = assertThrows(WrongInputException.class, ()->searchService.searchAll(keyword, address));

            //then
            assertEquals("address를 다시 입력해주세요.", exception.getMessage());
        }
        @Test
        void 통합검색_주소_다_안적음() {
            //given
            String keyword = "공차";
            String address = "서울 강북구";

            //when
            WrongInputException exception = assertThrows(WrongInputException.class, ()->searchService.searchAll(keyword, address));

            //then
            assertEquals("address를 다시 입력해주세요.", exception.getMessage());
        }
    }



}