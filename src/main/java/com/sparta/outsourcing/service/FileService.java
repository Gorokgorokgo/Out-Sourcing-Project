package com.sparta.outsourcing.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.sparta.outsourcing.dto.review.ReviewResponseDto;
import com.sparta.outsourcing.entity.Image;
import com.sparta.outsourcing.entity.ImageEnum;
import com.sparta.outsourcing.entity.Review;
import com.sparta.outsourcing.repository.CustomerRepository;
import com.sparta.outsourcing.repository.FileRepository;
import com.sparta.outsourcing.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final FileRepository fileRepository;
    private final ReviewRepository reviewRepository;


    // 기존의 단일 파일 업로드 메서드
    public Image uploadFiles(Long itemId, List<MultipartFile> files, ImageEnum imageEnum) throws IOException {

        for (MultipartFile file : files) {
            String fileName = generateFileName(file);
            String fileKey = imageEnum + "/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, inputStream, metadata);
                amazonS3.putObject(putObjectRequest);
            }
            String url = amazonS3.getUrl(bucketName, fileKey).toString();
            Image image = new Image(url, itemId, imageEnum);
            fileRepository.save(image);
        }
        return null;
    }


    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename().replace(" ", "_");
    }

    public ReviewResponseDto deleteReviewImages(Long imageId, Long customerId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NullPointerException("리뷰를 찾을 수 없습니다."));
        fileRepository.findById(imageId).orElseThrow(() -> new NullPointerException("사진을 찾을 수 없습니다."));
        if (!review.getCustomer().getCustomerId().equals(customerId))
            throw new NullPointerException("리뷰 작성자가 아닙니다.");
        fileRepository.deleteByImageId(imageId);
        return new ReviewResponseDto(review, fileRepository.findByItemIdAndImageEnum(reviewId, ImageEnum.REVIEW));
    }

}

