package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.customer.*;
import com.sparta.outsourcing.exception.customer.DifferentUsersException;
import com.sparta.outsourcing.exception.customer.WithdrawnMemberException;
import com.sparta.outsourcing.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/users/signup")
    public ResponseEntity<CustomerResponseDto> create(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        return ResponseEntity.ok(customerService.create(customerRequestDto));
    }

    @PostMapping("/users/login")
    public ResponseEntity<String> loginResponseDto(@Valid @RequestBody LoginRequestDto requestDto)  {
        String token = customerService.login(requestDto);
        return ResponseEntity.ok().header("Authorization", token).body("로그인 성공");
    }

    @GetMapping("/users")
    public ResponseEntity<CustomerResponseDto> myInformationView(@Auth AuthUser authUser) {
        return ResponseEntity.ok(customerService.myInformationView(authUser.getEmail()));
    }

    @PutMapping("/users")
    public ResponseEntity<CustomerResponseDto> customerInformationModify(@Auth AuthUser authUser,
                                                                         @Valid @RequestBody CustomerUpdateRequestDto updateRequestDto) {
        return ResponseEntity.ok(customerService.customerInformationModify(authUser.getEmail(), updateRequestDto));
    }

    @DeleteMapping("/users")
    public String delete(@Auth AuthUser authUser, @Valid @RequestBody LoginRequestDto loginRequestDto) throws DifferentUsersException {
        return customerService.delete(authUser.getEmail(), loginRequestDto);
    }
}
