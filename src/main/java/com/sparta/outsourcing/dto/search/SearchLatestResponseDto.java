package com.sparta.outsourcing.dto.search;

import com.sparta.outsourcing.dto.customer.CustomerSimpleResponseDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.Search;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchLatestResponseDto {
    private CustomerSimpleResponseDto customer;
    private List<SearchSimpleResponsDto> latestSearches;


    public SearchLatestResponseDto(Customer customer, List<Search> searchResult) {
        this.customer = new CustomerSimpleResponseDto(customer);
        this.latestSearches = searchResult.stream().map(SearchSimpleResponsDto::new).toList();
    }
}
