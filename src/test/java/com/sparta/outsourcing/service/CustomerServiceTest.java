package com.sparta.outsourcing.service;

import com.sparta.outsourcing.config.PasswordEncoder;
import com.sparta.outsourcing.constant.UserRoleEnum;
import com.sparta.outsourcing.dto.customer.CustomerRequestDto;
import com.sparta.outsourcing.dto.customer.CustomerResponseDto;
import com.sparta.outsourcing.dto.customer.CustomerUpdateRequestDto;
import com.sparta.outsourcing.dto.customer.LoginRequestDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.exception.*;
import com.sparta.outsourcing.jwt.JwtUtil;
import com.sparta.outsourcing.repository.CustomerRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private CustomerService customerService;

    private static final String ADMIN_TOKEN = "CorrectAdminToken";  // 이 값과 테스트 값이 일치하는지 확인

    @Nested
    class CustomerSignupTest {

        @Test
        void 중복된_이메일_종복() {
            // given
            Date date = new Date();
            Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);
            CustomerRequestDto customerRequestDto = new CustomerRequestDto();
            ReflectionTestUtils.setField(customerRequestDto, "email", "email@naver.com");
            given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));


            // when
            DataDuplicationException exception = assertThrows(DataDuplicationException.class, () -> {
                customerService.create(customerRequestDto);
            });

            // then
            assertEquals("중복된 이메일 입니다.", exception.getMessage());
        }


        @Test
        void 관리자_암호_틀리다() {
            // given
            Date date = new Date();

            // CustomerRequestDto 초기화
            CustomerRequestDto customerRequestDto = new CustomerRequestDto();

            ReflectionTestUtils.setField(customerRequestDto, "email", "email@naver.com");
            ReflectionTestUtils.setField(customerRequestDto, "admin", true);
            // 잘못된 관리자 토큰 설정 (ADMIN_TOKEN과 일치하지 않음)
            ReflectionTestUtils.setField(customerRequestDto, "adminToken", "AAABnvxRVklrnYdxKZ0aHgTBcXukeZygoC");

            // Mock 설정: 이메일이 중복되지 않도록 Optional.empty() 반환
            given(customerRepository.findByEmail(anyString())).willReturn(Optional.empty());

            // when: 관리자 암호가 틀렸을 때 예외 발생 확인
            InvalidAdminTokenException exception = assertThrows(InvalidAdminTokenException.class, () -> {
                customerService.create(customerRequestDto);
            });

            // then: 예외 메시지 확인
            assertEquals("관리자 암호가 틀려 등록이 불가능합니다.", exception.getMessage());
        }




        @Test
        void 일반_유저_회원가입_성공() {
            // given
            Date date = new Date();

            CustomerRequestDto customerRequestDto = new CustomerRequestDto();
            ReflectionTestUtils.setField(customerRequestDto, "email", "email@naver.com");
            ReflectionTestUtils.setField(customerRequestDto, "admin", false);
            given(customerRepository.findByEmail(anyString())).willReturn(Optional.empty());
            Customer savedCustomer = Customer.create(
                    "test",
                    "email@naver.com",
                    "password", date,
                    "address", UserRoleEnum.USER,
                    null
            );
            given(customerRepository.save(any(Customer.class))).willReturn(savedCustomer);

            CustomerResponseDto customerResponseDto = customerService.create(customerRequestDto);

            // then: 예외 메시지 확인
            assertEquals(UserRoleEnum.USER, customerResponseDto.getRoleEnum());
            assertEquals(customerRequestDto.getEmail(), customerResponseDto.getEmail());
        }

        @Test
        void 관리자_회원가입_성공() {
            // given
            Date date = new Date();

            // CustomerRequestDto 초기화
            CustomerRequestDto customerRequestDto = new CustomerRequestDto();

            ReflectionTestUtils.setField(customerRequestDto, "email", "email@naver.com");
            ReflectionTestUtils.setField(customerRequestDto, "admin", true);
            // 잘못된 관리자 토큰 설정 (ADMIN_TOKEN과 일치하지 않음)
            ReflectionTestUtils.setField(customerRequestDto, "adminToken", "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC");

            // Mock 설정: 이메일이 중복되지 않도록 Optional.empty() 반환
            given(customerRepository.findByEmail(anyString())).willReturn(Optional.empty());
            Customer customer = Customer.create(
                    "test",
                    "email@naver.com",
                    "password", date,
                    "address", UserRoleEnum.ADMIN,
                    null
            );
            given(customerRepository.save(any(Customer.class))).willReturn(customer);
            CustomerResponseDto customerResponseDto = customerService.create(customerRequestDto);


            assertEquals(UserRoleEnum.ADMIN, customerResponseDto.getRoleEnum());
            assertEquals(customerRequestDto.getEmail(), customerResponseDto.getEmail());
        }
        



    }

    @Nested
    class myInformationViewTest {
        @Test
        void 내정보_보기_실패() {
            // given
            given(customerRepository.findByEmail(anyString())).willReturn(Optional.empty());

            // when
            DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
                customerService.myInformationView(anyString());
            });

            // then
            assertEquals("선택한 유저는 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        void 내정보_보기_성공() {
            // given
            Date date = new Date();
            Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);
            given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));

            CustomerResponseDto customerResponseDto = customerService.myInformationView(anyString());

            assertEquals(customer.getEmail(), customerResponseDto.getEmail());
            assertEquals(customer.getRole(), customerResponseDto.getRoleEnum());
            assertEquals(customer.getAddress(), customerResponseDto.getAddress());
            assertEquals(customer.getBirthday(), customerResponseDto.getBirthday());
            assertEquals(customer.getName(), customerResponseDto.getName());



        }

        @Nested
        class CustomerInformationModifyTest {
            @Test
            void 내정보_수정_비밀번호_실패() {
                // given
                Date date = new Date();
                Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);
                CustomerUpdateRequestDto customerRequestDto = new CustomerUpdateRequestDto();
                ReflectionTestUtils.setField(customerRequestDto, "newPassword", "123");
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));

                PasswordMismatchException exception = assertThrows(PasswordMismatchException.class, () -> {
                    customerService.customerInformationModify(customer.getEmail(), customerRequestDto);
                });

                // then: 예외 메시지 확인
                assertEquals(customer.getEmail() + " 의 패스워드가 올바르지 않습니다.", exception.getMessage());
            }


            @Test
            void 비밀번호_변경_이전_비밀번호와_동일_에러() {
                // given
                Date date = new Date();
                Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);
                CustomerUpdateRequestDto customerRequestDto = new CustomerUpdateRequestDto();
                ReflectionTestUtils.setField(customerRequestDto, "newPassword", "123");
                ReflectionTestUtils.setField(customerRequestDto, "currentPassword", "123");
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));
                given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

                DataDuplicationException exception = assertThrows(DataDuplicationException.class, () -> {
                    customerService.customerInformationModify(customer.getEmail(), customerRequestDto);
                });

                // then: 예외 메시지 확인
                assertEquals("이전과 동일한 비밀번호입니다. 새로운 비밀번호를 입력해주세요.", exception.getMessage());
            }

            @Test
            void 비밀번호_변경_성공() {
                // given
                Date date = new Date();
                Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);

                CustomerUpdateRequestDto customerRequestDto = new CustomerUpdateRequestDto();
                ReflectionTestUtils.setField(customerRequestDto, "newPassword", "123");  // 새로운 비밀번호 설정
                ReflectionTestUtils.setField(customerRequestDto, "currentPassword", "password");  // 현재 비밀번호 설정 (실제 비밀번호와 일치해야 함)

                // Mock 설정: 이메일로 고객을 찾을 때 해당 고객 반환
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));

                // Mock 설정: 현재 비밀번호가 저장된 비밀번호와 일치하는지 확인
                given(passwordEncoder.matches("password", customer.getPassword())).willReturn(true);

                // Mock 설정: 새 비밀번호가 기존 비밀번호와 일치하지 않음 (즉, 변경 가능)
                given(passwordEncoder.matches("123", customer.getPassword())).willReturn(false);

                // Mock 설정: 새 비밀번호 인코딩 결과
                given(passwordEncoder.encode("123")).willReturn("encodedPassword");

                // when: 비밀번호 변경 로직 실행
                customerService.customerInformationModify(customer.getEmail(), customerRequestDto);

                // then: 고객 비밀번호가 업데이트되었는지 확인
                assertEquals("encodedPassword", customer.getPassword());
            }
        }

        @Nested
        class LoginTest {
            @Test
            void 로그인_유저_찾기_실패() {
                // given
                LoginRequestDto loginRequestDto = new LoginRequestDto();
                ReflectionTestUtils.setField(loginRequestDto, "email", "email@nave.com");
                ReflectionTestUtils.setField(loginRequestDto, "password", "password");
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.empty());


                DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
                    customerService.login(loginRequestDto);
                });


                assertEquals("선택한 유저는 존재하지 않습니다.", exception.getMessage());
            }

            @Test
            void 로그인_비밀번호_불일치() {
                // given
                Date date = new Date();
                LoginRequestDto loginRequestDto = new LoginRequestDto();
                Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);

                ReflectionTestUtils.setField(loginRequestDto, "email", "email@nave.com");
                ReflectionTestUtils.setField(loginRequestDto, "password", "password");
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));
                given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);


                PasswordMismatchException exception = assertThrows(PasswordMismatchException.class, () -> {
                    customerService.login(loginRequestDto);
                });

                assertEquals(loginRequestDto.getEmail() + "의 패스워드가 올바르지 않습니다.", exception.getMessage());
            }

            @Test
            void 탈퇴유저_로그인_시도() {
                // given
                Date date = new Date();
                LocalDateTime deleteTime = LocalDateTime.now();
                LoginRequestDto loginRequestDto = new LoginRequestDto();
                Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);
                ReflectionTestUtils.setField(customer, "dateDeleted", deleteTime);

                ReflectionTestUtils.setField(loginRequestDto, "email", "email@nave.com");
                ReflectionTestUtils.setField(loginRequestDto, "password", "password");
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));
                given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);


                WithdrawnMemberException exception = assertThrows(WithdrawnMemberException.class, () -> {
                    customerService.login(loginRequestDto);
                });

                assertEquals("이미 탈퇴한 회원 입니다.", exception.getMessage());
            }

            @Test
            void 로그인_성공() throws WithdrawnMemberException {
                // given
                Date date = new Date();
                LoginRequestDto loginRequestDto = new LoginRequestDto();
                Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);

                ReflectionTestUtils.setField(loginRequestDto, "email", "email@nave.com");
                ReflectionTestUtils.setField(loginRequestDto, "password", "password");
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));
                given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);


                String login = customerService.login(loginRequestDto);


                assertEquals(jwtUtil.createToken(customer.getCustomerId(), customer.getEmail(), customer.getRole()), login);
            }
        }

        @Nested
        class Delete {
            @Test
            void 삭제_실패_AuthUser_입력_이메일_불일치() {
                // given
                String email = "user@example.com";
                String differentEmail = "different@example.com";
                String password = "password123";

                // 로그인 요청 DTO 설정
                LoginRequestDto loginRequestDto = new LoginRequestDto();
                ReflectionTestUtils.setField(loginRequestDto, "email", differentEmail);
                ReflectionTestUtils.setField(loginRequestDto, "password", password);

                // Customer 객체 생성
                Customer customer = Customer.create("test", differentEmail, "encodedPassword", new Date(), "address", UserRoleEnum.USER, null);

                // Mock 설정
                given(customerRepository.findByEmail(differentEmail)).willReturn(Optional.of(customer));

                // when & then
                DifferentUsersException exception = assertThrows(DifferentUsersException.class, () -> {
                    customerService.delete(email, loginRequestDto);
                });

                // 예외 메시지 검증
                assertEquals("로그인 사용자와 일치 하지 않습니다.", exception.getMessage());
            }

            @Test
            void 삭제시_비밀번호_불일치() {
                // given
                String email = "user@example.com";
                String password = "password123";

                // 로그인 요청 DTO 설정
                LoginRequestDto loginRequestDto = new LoginRequestDto();
                ReflectionTestUtils.setField(loginRequestDto, "email", email);
                ReflectionTestUtils.setField(loginRequestDto, "password", password);

                // Customer 객체 생성
                Customer customer = Customer.create("test", email, "encodedPassword", new Date(), "address", UserRoleEnum.USER, null);

                // Mock 설정
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));
                given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

                // when & then
                PasswordMismatchException exception = assertThrows(PasswordMismatchException.class, () -> {
                    customerService.delete(email, loginRequestDto);
                });

                // 예외 메시지 검증
                assertEquals(loginRequestDto.getEmail() + "의 패스워드가 올바르지 않습니다.", exception.getMessage());
            }

            @Test
            void 삭제_성공() throws DifferentUsersException {
                // given
                String email = "user@example.com";
                String password = "password123";

                // 로그인 요청 DTO 설정
                LoginRequestDto loginRequestDto = new LoginRequestDto();
                ReflectionTestUtils.setField(loginRequestDto, "email", email);
                ReflectionTestUtils.setField(loginRequestDto, "password", password);

                // Customer 객체 생성
                Customer customer = Customer.create("test", email, "encodedPassword", new Date(), "address", UserRoleEnum.USER, null);

                // Mock 설정
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));
                given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

                // when & then
                customerService.delete(email, loginRequestDto);

                // 예외 메시지 검증
            }
        }


        @Nested
        class FindUser {

            @Test
            void 유저_찾기_실패() {
                // given
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.empty());

                // when
                DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
                    customerService.findUser(anyString());
                });

                // then
                assertEquals("선택한 유저는 존재하지 않습니다.", exception.getMessage());
            }

            @Test
            void 유저_찾기중_탈퇴유저() {
                // given
                Date date = new Date();
                Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);
                ReflectionTestUtils.setField(customer, "dateDeleted", LocalDateTime.now());
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));
                // when
                DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
                    customerService.findUser(anyString());
                });
                // then
                assertEquals("이미 탈퇴된 유저 입니다", exception.getMessage());
            }

            @Test
            void 유저_찾기_성공() {
                // given
                Date date = new Date();
                Customer customer = Customer.create("test", "email@naver.com", "password", date, "address", UserRoleEnum.USER, null);
                given(customerRepository.findByEmail(anyString())).willReturn(Optional.of(customer));
                // when
                Customer user = customerService.findUser(anyString());
                // then
                assertEquals(customer, user);
                assertEquals(customer.getEmail(), user.getEmail());
                assertEquals(customer.getPassword(), user.getPassword());
                assertEquals(customer.getRole(), user.getRole());
                assertEquals(customer.getDateDeleted(), user.getDateDeleted());
                assertEquals(customer.getNaverId(), user.getNaverId());
                assertEquals(customer.getBirthday(), user.getBirthday());
                assertEquals(customer.getAddress(), user.getAddress());
                assertEquals(customer.getCustomerId(), user.getCustomerId());
                assertEquals(customer.getName(), user.getName());
            }
        }
    }
}