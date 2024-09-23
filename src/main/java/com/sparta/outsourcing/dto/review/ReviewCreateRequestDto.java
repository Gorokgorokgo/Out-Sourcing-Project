package com.sparta.outsourcing.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewCreateRequestDto {

    @NotNull(message = "가게 아이디는 필수값입니다.")
    private Long storeId;

    @NotNull(message = "별점은 필수값입니다.")
    @Min(1)
    @Max(5)
    private Integer star;

    @NotNull(message = "내용은 필수값입니다.(공백 허용)")
    @Size(max=255, message = "글자 한도를 초과하였습니다.")
    private  String content;

    public ReviewCreateRequestDto(Long storeId, Integer star, String content) {
        this.storeId = storeId;
        this.star = star;
        this.content = content;
    }
}
