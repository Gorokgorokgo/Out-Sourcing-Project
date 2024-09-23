package com.sparta.outsourcing.dto.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverProfileResponseDto {

    private String resultcode;
    private String message;
    private NaverUserProfile response; // 대문자로 클래스 이름 수정

    @JsonCreator
    public NaverProfileResponseDto(
            @JsonProperty("resultcode") String resultcode,
            @JsonProperty("message") String message,
            @JsonProperty("response") NaverUserProfile response) {
        this.resultcode = resultcode;
        this.message = message;
        this.response = response;
    }
}
