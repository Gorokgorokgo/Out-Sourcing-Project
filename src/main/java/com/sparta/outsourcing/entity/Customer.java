package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.dto.customer.CustomerUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@Table(name = "customers")
@NoArgsConstructor
public class Customer extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @Column(name = "birthday", nullable = false)
    private Date birthday;

    @Column(name = "address", length = 50)
    private String address;

    @Column(name = "deleted_at")
    private LocalDateTime dateDeleted;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(unique = true)
    // naverId 필드를 데이터베이스 컬럼과 매핑하며, 해당 값은 유일해야 함 (카카오 ID는 중복 불가)
    private String naverId;

    // 정적 팩토리 메서드
    public static Customer create(String name, String email, String password, Date birthday, String address, UserRoleEnum role, String naverId) {
        Customer customer = new Customer();
        customer.name = name;
        customer.email = email;
        customer.password = password;
        customer.birthday = birthday;
        customer.address = address;
        customer.role = role;
        customer.dateDeleted = null; // 삭제되지 않았음을 나타내기 위해 null로 설정
        customer.naverId = naverId; // 카카오 ID를 저장
        return customer;
    }


    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void membershipWithdrawalTime(LocalDateTime deleteTime) {
        this.dateDeleted = deleteTime;
    }

    public void update(CustomerUpdateRequestDto updateRequestDto) {
        if (updateRequestDto.getBirthday() != null) this.birthday = updateRequestDto.getBirthday();
        if (updateRequestDto.getAddress() != null) this.address = updateRequestDto.getAddress();
    }

    public Customer naverIdUpdate(String naverId) {
        this.naverId = naverId; // 기존 사용자 객체의 카카오 ID를 업데이트
        return this; // 업데이트된 객체를 반환
    }
}
