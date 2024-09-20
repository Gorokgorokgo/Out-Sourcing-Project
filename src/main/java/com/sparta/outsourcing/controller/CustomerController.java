package com.sparta.outsourcing.controller;

import com.sparta.outsourcing.annotation.Auth;
import com.sparta.outsourcing.dto.customer.*;
import com.sparta.outsourcing.exception.DifferentUsersException;
import com.sparta.outsourcing.exception.WithdrawnMemberException;
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
    public String create(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        ResponseEntity.status(HttpStatus.OK).body(customerService.create(customerRequestDto));
        return "redirect:/api/users/login";
    }

    @PostMapping("/users/login")
    public ResponseEntity<Object> loginResponseDto(@Valid @RequestBody LoginRequestDto requestDto) throws WithdrawnMemberException {
        return ResponseEntity.status(HttpStatus.OK).header("Authorization",customerService.login(requestDto)).build();
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
