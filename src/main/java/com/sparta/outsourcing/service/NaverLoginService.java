package com.sparta.outsourcing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sparta.outsourcing.config.PasswordEncoder;
import com.sparta.outsourcing.dto.customer.NaverProfileResponseDto;
import com.sparta.outsourcing.dto.customer.NaverTokenDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.UserRoleEnum;
import com.sparta.outsourcing.jwt.JwtUtil;
import com.sparta.outsourcing.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverLoginService {

    // 네이버 OAuth 클라이언트 ID와 Secret 값을 application.properties에서 가져옴
    @Value("${naver.client.id}")
    private String CLIENT_ID;
    @Value("${naver.client.secret}")
    private String CLIENT_SECRET;

    private final JwtUtil jwtUtil;

    private final CustomerRepository customerRepository;

    // RestTemplate은 HTTP 요청을 위한 스프링 제공 클래스
    private final RestTemplate restTemplate; // RestTemplate 객체를 클래스 레벨로 이동

    private final PasswordEncoder passwordEncoder;

    // 네이버 로그인 URL 생성
    public String createNaverURL() throws UnsupportedEncodingException {
        // Redirect URI 설정
        String redirectURI = URLEncoder.encode("http://localhost:8080/api/naver/login", "UTF-8");
        // CSRF 방지용 state 값 생성 (랜덤 값)
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();

        // 네이버 인증 URL 빌드
        return buildNaverAuthUrl(redirectURI, state);
    }

    // 네이버 로그인 처리 (토큰 발급 및 프로필 조회)
    public String loginNaver(String code, String state, HttpServletResponse response) throws JsonProcessingException, ParseException {
        // 네이버 토큰 요청
        NaverTokenDto naverToken = requestNaverToken(code, state);

        // 네이버 사용자 프로필 요청
        NaverProfileResponseDto naverProfile = requestNaverProfile(naverToken.getAccess_token());

        Customer customerResponseDto = registerNaverUserIfNeeded(naverProfile);

        String token = jwtUtil.createToken(customerResponseDto.getCustomerId(), customerResponseDto.getEmail(), customerResponseDto.getRole());
        return token;
    }

    private Customer registerNaverUserIfNeeded(NaverProfileResponseDto naverProfile) throws ParseException {

        Customer customer = customerRepository.findByNaverId(naverProfile.getResponse().getId());


        if (customer == null) {
            String email = naverProfile.getResponse().getEmail();
            Customer sameEmailUser = customerRepository.findByEmail(email).orElse(null);

            if (sameEmailUser != null) {
                customer = sameEmailUser;
                // 기존 회원의 네이버 ID 업데이트
                customer.naverIdUpdate(naverProfile.getResponse().getId());
            }else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString(); // 비밀번호를 UUID로 생성
                String encodedPassword = passwordEncoder.encode(password); // 비밀번호를 인코딩

                String birthday = naverProfile.getResponse().getBirthyear()+"-"+naverProfile.getResponse().getBirthday();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date conversion = dateFormat.parse(birthday);

                customer = Customer.create(
                        naverProfile.getResponse().getName(),
                        email,
                        encodedPassword,
                        conversion,
                        null,
                        UserRoleEnum.USER,
                        naverProfile.getResponse().getId()
                );


            }
            customerRepository.save(customer);
        }
        return customer;
    }

    // 네이버 OAuth 토큰을 요청하는 메소드
    private NaverTokenDto requestNaverToken(String code, String state) throws JsonProcessingException {
        // 네이버 API 요청 시 필요한 HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");

        // 네이버 토큰 요청에 필요한 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("code", code);
        params.add("state", state);

        // HTTP 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // 네이버 토큰 요청을 POST로 수행하고 응답을 받음
        ResponseEntity<String> tokenResponse = restTemplate.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // 응답된 JSON 문자열을 NaverTokenVo 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(tokenResponse.getBody(), NaverTokenDto.class);
    }

    // 네이버 사용자 프로필을 요청하는 메소드
    private NaverProfileResponseDto requestNaverProfile(String accessToken) throws JsonProcessingException {
        // 네이버 API 요청 시 필요한 HTTP 헤더 설정 (Bearer 토큰 사용)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");

        // HTTP 요청 엔티티 생성
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // 네이버 사용자 프로필 요청을 GET으로 수행하고 응답을 받음
        ResponseEntity<String> profileResponse = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // 응답된 JSON 문자열을 NaverProfileVo 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(profileResponse.getBody(), NaverProfileResponseDto.class);
    }

    // 네이버 인증 URL 빌드 메소드
    private String buildNaverAuthUrl(String redirectURI, String state) {
        // 네이버 인증 URL 생성 (필요한 파라미터를 포함)
        StringBuffer url = new StringBuffer();
        url.append("https://nid.naver.com/oauth2.0/authorize?response_type=code");
        url.append("&client_id=").append(CLIENT_ID);
        url.append("&state=").append(state);
        url.append("&redirect_uri=").append(redirectURI);

        return url.toString();
    }
}
