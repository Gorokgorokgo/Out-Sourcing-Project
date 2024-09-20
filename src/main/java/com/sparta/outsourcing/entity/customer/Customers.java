package com.sparta.outsourcing.entity.customer;

import com.sparta.outsourcing.entity.Timestamped;
import com.sparta.outsourcing.entity.UserRoleEnum;
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
public class Customers extends Timestamped {


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

    @Column(name = "date_deleted")
    private LocalDateTime dateDeleted;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    // 정적 팩토리 메서드
    public static Customers create(String name, String email, String password, Date birthday, String address, UserRoleEnum role) {
        Customers customer = new Customers();
        customer.name = name;
        customer.email = email;
        customer.password = password;
        customer.birthday = birthday;
        customer.address = address;
        customer.role = role;
        customer.dateDeleted = null; // 삭제되지 않았음을 나타내기 위해 null로 설정
        return customer;
    }


    public void updatePassword(String newPassword){
        this.password = newPassword;
    }

    public void deleteUpdate(LocalDateTime deleteTime){
        this.dateDeleted = deleteTime;
    }

}
