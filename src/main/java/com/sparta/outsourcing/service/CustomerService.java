package com.sparta.outsourcing.service;

import com.sparta.outsourcing.config.PasswordEncoder;
import com.sparta.outsourcing.dto.customer.CustomerRequestDto;
import com.sparta.outsourcing.dto.customer.CustomerResponseDto;
import com.sparta.outsourcing.dto.customer.CustomerUpdateRequestDto;
import com.sparta.outsourcing.dto.customer.LoginRequestDto;
import com.sparta.outsourcing.entity.Customers;
import com.sparta.outsourcing.entity.UserRoleEnum;
import com.sparta.outsourcing.exception.*;
import com.sparta.outsourcing.jwt.JwtUtil;
import com.sparta.outsourcing.repository.CustomersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomersRepository customersRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    private final JwtUtil jwtUtil;

    public CustomerResponseDto create(CustomerRequestDto customerRequestDto) {

        String password = passwordEncoder.encode(customerRequestDto.getPassword());
        Optional<Customers> checkEmail = customersRepository.findByEmail(customerRequestDto.getEmail());
        if (checkEmail.isPresent()) throw new DataDuplicationException("중복된 이메일 입니다.");


        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (customerRequestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(customerRequestDto.getAdminToken())) {
                throw new DataNotFoundException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        Customers customers = Customers.create(
                customerRequestDto.getName(),
                customerRequestDto.getEmail(),
                password,
                customerRequestDto.getBirthday(),
                customerRequestDto.getAddress(),
                role
        );

        Customers saveCustomers = customersRepository.save(customers);

        return new CustomerResponseDto(saveCustomers);
    }

    public CustomerResponseDto myInformationView(String email) {

        Customers user = findUser(email);

        return new CustomerResponseDto(user);

    }

    @Transactional
    public CustomerResponseDto customerInformationModify(String email, CustomerUpdateRequestDto updateRequestDto) {

        Customers customers = findUser(email);

        if (updateRequestDto.getNewPassword() != null) {

            if (!passwordEncoder.matches(updateRequestDto.getCurrentPassword(), customers.getPassword())) {
                throw new PasswordMismatchException(email + " 의 패스워드가 올바르지 않습니다.");
            }

            if (passwordEncoder.matches(updateRequestDto.getNewPassword(), customers.getPassword())) {
                throw new DataDuplicationException("이전과 동일한 비밀번호입니다. 새로운 비밀번호를 입력해주세요.");
            }

            String password = passwordEncoder.encode(updateRequestDto.getNewPassword());
            customers.updatePassword(password);
        }

        customers.update(updateRequestDto);

        return new CustomerResponseDto(customers);

    }

    public String delete(String email, LoginRequestDto loginRequestDto) {
        Customers customers = findUser(loginRequestDto.getEmail());

        if (!email.equals(customers)) {
            throw new DifferentUsersException("로그인 사용자와 일치 하지 않습니다.");
        }

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), customers.getPassword())) {
            throw new PasswordMismatchException(loginRequestDto.getEmail() + "의 패스워드가 올바르지 않습니다.");
        }

        customers.deleteUpdate(java.time.LocalDateTime.now());

        return "삭제 완료";
    }

    public String login(LoginRequestDto requestDto) throws WithdrawnMemberException {
        //입력된 이메일로 유저찾기
        Customers customers = customersRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new DataNotFoundException("선택한 유저는 존재하지 않습니다."));

        //비밀번호 일치하는지 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), customers.getPassword())) {
            throw new PasswordMismatchException(requestDto.getEmail() + "의 패스워드가 올바르지 않습니다.");
        }

        //탈퇴유저 로그인 방지
        if (customers.getDateDeleted() == null) {

            //존재하는 유저가 비밀번호를 알맞게 입력시 JWT토큰반환
            return jwtUtil.createToken(
                    customers.getCustomersId(),
                    customers.getEmail(),
                    customers.getRole()
            );
        } else {
            throw new WithdrawnMemberException("이미 탈퇴한 회원 입니다.");
        }
    }

    private Customers findUser(String email) {
        Customers user = customersRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("선택한 유저는 존재하지 않습니다."));
        if (user.getDateDeleted() != null) {
            throw new DataNotFoundException("이미 삭제된 유저 입니다");
        }

        return user;
    }
}
