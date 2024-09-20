package com.sparta.outsourcing.sevice;

import com.sparta.outsourcing.dto.StoreRequestDto;
import com.sparta.outsourcing.dto.StoreResponseDto;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;


    @Transactional
    public StoreResponseDto create(StoreRequestDto requestDto) {
        Store saveStore = storeRepository.save(new Store(requestDto));
        return new StoreResponseDto(saveStore);
    }

    public StoreResponseDto getStore(Long storeId) {
        storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("가게가 없습니다.")
        );
        return new StoreResponseDto(storeRepository.findById(storeId).orElseThrow());
    }

    @Transactional
    public Page<StoreResponseDto> getStores(Pageable pageable) {
        Page<Store> stores = storeRepository.findAll(pageable);
        return stores.map(StoreResponseDto::new);
    }


    @Transactional
    public StoreResponseDto update(Long storeId, StoreRequestDto requestDto) {
        Store findStore = storeRepository.findById(storeId).orElseThrow();
        findStore.update(requestDto);
        return new StoreResponseDto(findStore);
    }

    @Transactional
    public void delete(Long storeId) {
        Store findStore = storeRepository.findById(storeId).orElseThrow();
        storeRepository.delete(findStore);
    }
}
