package com.sparta.outsourcing.dto.search;

import com.sparta.outsourcing.entity.Search;
import lombok.Getter;

@Getter
public class SearchSimpleResponsDto {
    private Long searchId;
    private String keyword;

    public SearchSimpleResponsDto(Search search) {
        this.searchId = search.getSearchId();
        this.keyword = search.getKeyword();
    }
}
