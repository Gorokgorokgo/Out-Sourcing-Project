package com.sparta.outsourcing.dto.search;

import com.sparta.outsourcing.dto.customer.CustomerSimpleResponseDto;
import com.sparta.outsourcing.entity.Search;
import lombok.Getter;

@Getter
public class SearchResponsDto {
    private Long SearchId;
    private String Keyword;
    private CustomerSimpleResponseDto customer;

    public SearchResponsDto(Search savedSearch) {
        this.SearchId = savedSearch.getSearchId();
        this.Keyword = savedSearch.getKeyword();
        this.customer = new CustomerSimpleResponseDto(savedSearch.getCustomer());
    }
}
