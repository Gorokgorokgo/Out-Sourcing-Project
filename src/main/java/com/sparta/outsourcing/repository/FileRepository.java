package com.sparta.outsourcing.repository;

import com.sparta.outsourcing.entity.Image;
import com.sparta.outsourcing.entity.ImageEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<Image, Long> {

    List<Image> findByItemIdAndImageEnum(Long itemId, ImageEnum imageEnum);

    void deleteByImageId(Long imageId);


    void findByItemId(Long reviewId);
}
