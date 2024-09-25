package com.sparta.outsourcing.dto.file;

import com.sparta.outsourcing.entity.Image;
import com.sparta.outsourcing.entity.ImageEnum;
import lombok.Getter;

@Getter
public class FileResponseDto {
    private Long imageId;
    private Long itemId;
    private String url;
    private ImageEnum imageEnum;

    public FileResponseDto(Image file) {
        this.imageId = file.getImageId();
        this.itemId = file.getItemId();
        this.url = file.getUrl();
        this.imageEnum = file.getImageEnum();
    }
}
