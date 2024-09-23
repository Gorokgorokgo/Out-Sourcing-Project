package com.sparta.outsourcing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.naming.Name;

@Getter
@Entity
@Table(name = "images")
@NoArgsConstructor
public class Image extends Timestamped2{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Column(name = "item_id", nullable = false, length = 50)
    private Long itemId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ImageEnum imageEnum;


    public Image(String url, Long itemId, ImageEnum imageEnum) {
        this.url = url;
        this.itemId = itemId;
        this.imageEnum = imageEnum;
    }
}
