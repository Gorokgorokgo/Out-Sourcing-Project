package com.sparta.outsourcing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.outsourcing.dto.customer.CustomerResponseDto;
import com.sparta.outsourcing.dto.customer.NaverProfileResponseDto;
import com.sparta.outsourcing.service.NaverLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NaverLoginController {

    private final NaverLoginService naverService;

    @GetMapping("/naver/oauth")
    public ResponseEntity<?> naverConnect() throws UnsupportedEncodingException {
        String url = naverService.createNaverURL();

        return new ResponseEntity<>(url, HttpStatus.OK); // 프론트 브라우저로 보내는 주소
    }

    @GetMapping("/naver/login")
    public ResponseEntity<Object> naverLogin(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response) throws JsonProcessingException, ParseException {
        return ResponseEntity.status(HttpStatus.OK).header("Authorization", naverService.loginNaver(code, state, response)).build();
    }
}
