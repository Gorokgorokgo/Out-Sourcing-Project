package com.sparta.outsourcing.service;

import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.exception.NotFoundException;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;

    public Customer findCustomerById(Long customerId) {
        return customerRepository.findByCustomerId(customerId).orElseThrow(() -> new NotFoundException("해당 고객이 존재하지 않습니다."));
    }

    public Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException("해당하는 매장이 존재하지 않습니다."));
    }


}
