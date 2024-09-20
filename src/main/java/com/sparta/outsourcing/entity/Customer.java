package com.sparta.outsourcing.entity;

import com.sparta.outsourcing.dto.customer.CustomerUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@Table(name = "customers")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Customer extends Timestamped {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customers_id")
    private Long customersId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @Column(name = "birthday", nullable = false)
    private Date birthday;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "deleted_at")
    private LocalDateTime dateDeleted;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    // 정적 팩토리 메서드
    public static Customer create(String name, String email, String password, Date birthday, String address, UserRoleEnum role) {
        Customer customer = new Customer();
        customer.name = name;
        customer.email = email;
        customer.password = password;
        customer.birthday = birthday;
        customer.address = address;
        customer.role = role;
        customer.dateDeleted = null; // 삭제되지 않았음을 나타내기 위해 null로 설정
        return customer;
    }


    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void deleteUpdate(LocalDateTime deleteTime) {
        this.dateDeleted = deleteTime;
    }

    public void update(CustomerUpdateRequestDto updateRequestDto) {
        if (updateRequestDto.getBirthday() != null) this.birthday = updateRequestDto.getBirthday();
        if (updateRequestDto.getAddress() != null) this.address = updateRequestDto.getAddress();
    }
}
