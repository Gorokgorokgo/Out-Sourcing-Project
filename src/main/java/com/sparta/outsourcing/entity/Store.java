package com.sparta.outsourcing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Setter
@Table(name="stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @OneToMany (mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    List<Menu> menuList = new ArrayList<>();

    public Store(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}
