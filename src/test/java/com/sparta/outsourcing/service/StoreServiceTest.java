package com.sparta.outsourcing.service;

import com.sparta.outsourcing.constant.UserRoleEnum;
import com.sparta.outsourcing.dto.store.StoreRequestDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private StoreServiceTest storeServiceTest;

    @Nested
    class CreateStoreTest {

        /**
         *     @Transactional
         *     public StoreResponseDto createStore(AuthUser authUser, StoreRequestDto requestDto) {
         *         Customer findCustomer = getFindCustomer(authUser);  // 사용자 확인
         *
         *         checkAdminAuthority(authUser); // ADMIN 인지 확인
         *         checkStoreLimit(authUser);  // 가게 수 확인
         *
         *         Store newStore = storeRepository.save(new Store(findCustomer, requestDto));
         *         return new StoreResponseDto(newStore);
         *     }
         */
        @Test
        public void 뭐라해_없음() {
            //given
            long customerId = 1L;
            long storeId = 2L;

            Date date = new Date();
            Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);

            StoreRequestDto storeRequestDto = new StoreRequestDto();
            Store store = new Store(customer, storeRequestDto);

            ReflectionTestUtils.setField(customer, "customerId", customerId);
            ReflectionTestUtils.setField(store, "storeId", storeId);

            



        }
    }

}
