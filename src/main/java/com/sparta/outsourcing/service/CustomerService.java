package com.sparta.outsourcing.service;

import com.sparta.outsourcing.config.PasswordEncoder;
import com.sparta.outsourcing.dto.customer.CustomerRequestDto;
import com.sparta.outsourcing.dto.customer.CustomerResponseDto;
import com.sparta.outsourcing.dto.customer.CustomerUpdateRequestDto;
import com.sparta.outsourcing.dto.customer.LoginRequestDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.UserRoleEnum;
import com.sparta.outsourcing.exception.*;
import com.sparta.outsourcing.jwt.JwtUtil;
import com.sparta.outsourcing.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customersRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    private final JwtUtil jwtUtil;

    public CustomerResponseDto create(CustomerRequestDto customerRequestDto) {

        String password = passwordEncoder.encode(customerRequestDto.getPassword());
        Optional<Customer> checkEmail = customersRepository.findByEmail(customerRequestDto.getEmail());
        if (checkEmail.isPresent()) throw new DataDuplicationException("중복된 이메일 입니다.");


        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (customerRequestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(customerRequestDto.getAdminToken())) {
                throw new InvalidAdminTokenException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        Customer customer = Customer.create(
                customerRequestDto.getName(),
                customerRequestDto.getEmail(),
                password,
                customerRequestDto.getBirthday(),
                customerRequestDto.getAddress(),
                role,
                null);
        Customer saveCustomer = customersRepository.save(customer);

        return new CustomerResponseDto(saveCustomer);
    }

    public CustomerResponseDto myInformationView(String email) {

        Customer customer = findUser(email);

        return new CustomerResponseDto(customer);

    }

    @Transactional
    public CustomerResponseDto customerInformationModify(String email, CustomerUpdateRequestDto updateRequestDto) {

        Customer customer = findUser(email);

        if (updateRequestDto.getNewPassword() != null) {

            if (!passwordEncoder.matches(updateRequestDto.getCurrentPassword(), customer.getPassword())) {
                throw new PasswordMismatchException(email + " 의 패스워드가 올바르지 않습니다.");
            }

            if (passwordEncoder.matches(updateRequestDto.getNewPassword(), customer.getPassword())) {
                throw new DataDuplicationException("이전과 동일한 비밀번호입니다. 새로운 비밀번호를 입력해주세요.");
            }

            String password = passwordEncoder.encode(updateRequestDto.getNewPassword());
            customer.updatePassword(password);
        }

        customer.update(updateRequestDto);

        return new CustomerResponseDto(customer);

    }

    public String delete(String email, LoginRequestDto loginRequestDto) throws DifferentUsersException {
        Customer customer = findUser(loginRequestDto.getEmail());

        if (!email.equals(customer.getEmail())) {
            throw new DifferentUsersException("로그인 사용자와 일치 하지 않습니다.");
        }

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), customer.getPassword())) {
            throw new PasswordMismatchException(loginRequestDto.getEmail() + "의 패스워드가 올바르지 않습니다.");
        }

        customer.membershipWithdrawalTime(java.time.LocalDateTime.now());

        return "삭제 완료";
    }

    public String login(LoginRequestDto requestDto) throws WithdrawnMemberException {
        //입력된 이메일로 유저찾기
        Customer customer = customersRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new DataNotFoundException("선택한 유저는 존재하지 않습니다."));

        //비밀번호 일치하는지 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), customer.getPassword())) {
            throw new PasswordMismatchException(requestDto.getEmail() + "의 패스워드가 올바르지 않습니다.");
        }

        //탈퇴유저 로그인 방지
        if (customer.getDateDeleted() == null) {

            //존재하는 유저가 비밀번호를 알맞게 입력시 JWT토큰반환
            return jwtUtil.createToken(
                    customer.getCustomerId(),
                    customer.getEmail(),
                    customer.getRole()
            );
        } else {
            throw new WithdrawnMemberException("이미 탈퇴한 회원 입니다.");
        }
    }

     Customer findUser(String email) {
        Customer customer = customersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("선택한 유저는 존재하지 않습니다."));
        if (customer.getDateDeleted() != null) {
            throw new DataNotFoundException("이미 탈퇴된 유저 입니다");
        }
        return customer;
    }
}
