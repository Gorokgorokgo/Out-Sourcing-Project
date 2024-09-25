package com.sparta.outsourcing.service;

import com.sparta.outsourcing.constant.UserRoleEnum;
import com.sparta.outsourcing.dto.customer.AuthUser;
import com.sparta.outsourcing.dto.store.StoreRequestDto;
import com.sparta.outsourcing.dto.store.StoreResponseDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    StoreRepository storeRepository;

    @InjectMocks
    StoreService storeService;

    @Test
    @DisplayName("가게 생성 성공 테스트")
    public void createStoreSuccess() {
        // Given
        StoreRequestDto requestDto = new StoreRequestDto("Test Store", 10000, true, "Seoul", LocalTime.of(9, 0), LocalTime.of(22, 0));
        AuthUser authUser = new AuthUser(1L, "admin@test.com", UserRoleEnum.ADMIN);

        Customer customer1 = new Customer();  // Mock된 Customer 엔티티
        ReflectionTestUtils.setField(customer1, "customerId", 1L);


        Store store = new Store(customer1, requestDto);  // 가게 엔티티 생성
        StoreResponseDto responseDto = new StoreResponseDto(store);

        // when: Mock 리포지토리가 메서드 호출될 때 반환할 값을 설정
        when(customerRepository.findById(authUser.getCustomerId())).thenReturn(Optional.of(customer1));
        when(storeRepository.countByCustomer_CustomerIdAndStoreStatus(authUser.getCustomerId(), true)).thenReturn(1);
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        // 실제로 service에서 메서드를 호출
        StoreResponseDto createdStore = storeService.createStore(authUser, requestDto);

        // then: 결과 확인
        assertNotNull(createdStore);  // 가게가 성공적으로 생성되었는지 확인
        assertEquals(requestDto.getStoreName(), createdStore.getStoreName());
        assertEquals(requestDto.getMinPrice(), createdStore.getMinPrice());
        assertEquals(requestDto.isStoreStatus(), createdStore.isStoreStatus());
        assertEquals(requestDto.getAddress(), createdStore.getAddress());
        assertEquals(requestDto.getOpenTime(), createdStore.getOpenTime());
        assertEquals(requestDto.getCloseTime(), createdStore.getCloseTime());

        // verify: 리포지토리 메서드가 호출된 횟수 및 파라미터 확인
        verify(customerRepository, times(1)).findById(authUser.getCustomerId());  // 고객이 조회되었는지 확인
        verify(storeRepository, times(1)).save(any(Store.class));  // 가게가 저장되었는지 확인
    }
}