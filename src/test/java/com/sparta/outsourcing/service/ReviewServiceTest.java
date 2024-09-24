package com.sparta.outsourcing.service;

import com.sparta.outsourcing.constant.UserRoleEnum;
import com.sparta.outsourcing.dto.review.ReviewCreateRequestDto;
import com.sparta.outsourcing.dto.review.ReviewResponseDto;
import com.sparta.outsourcing.dto.review.ReviewSimpleResponseDto;
import com.sparta.outsourcing.dto.review.ReviewUpdateRequestDto;
import com.sparta.outsourcing.entity.Customer;
import com.sparta.outsourcing.entity.ImageEnum;
import com.sparta.outsourcing.entity.Review;
import com.sparta.outsourcing.entity.Store;
import com.sparta.outsourcing.exception.DifferentUsersException;
import com.sparta.outsourcing.exception.NotFoundException;
import com.sparta.outsourcing.repository.FileRepository;
import com.sparta.outsourcing.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.sparta.outsourcing.entity.Customer.create;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    ReviewRepository reviewRepository;

    @Mock
    FileService fileService;

    @Mock
    FileRepository fileRepository;

    @InjectMocks
    ReviewService reviewService;

    @Mock
    CommonService commonService;

    private Customer customer1;
    private Customer customer2;
    private Store store1;
    List<MultipartFile> files = new ArrayList<>();
    private Review review1;

    private Store store2;


    @BeforeEach
    void setUp() {

        customer1 = create("유리아", "Yuria123@naver.com", "SHIGWE5GSJVF4DHSH", new Date(), "서울 강북구 수유동 8", UserRoleEnum.USER, "yuria159");
        customer2 = create("사리나", "Rubi123@naver.com", "AHG48XFHD6C5F16DR6E45C", new Date(), "서울 강북구 미아동 789", UserRoleEnum.USER, "rubi42");
        ReflectionTestUtils.setField(customer1, "customerId", 1L);
        ReflectionTestUtils.setField(customer2, "customerId", 2L);
        store1 = spy(Store.class);
        ReflectionTestUtils.setField(store1, "storeId", 1L );
        ReflectionTestUtils.setField(store1, "storeName", "공차 미아뉴타운점");
        ReflectionTestUtils.setField(store1, "address", "서울 강북구 미아동 812");
        ReviewCreateRequestDto requestDto1 = new ReviewCreateRequestDto(store1.getStoreId(), 3,"test");
        review1 = new Review(customer1, store1, requestDto1);
        ReflectionTestUtils.setField(review1, "reviewId", 1L);
    }
    @Nested
    class reviewCreateTest{
        @Test
        void 리뷰_생성_성공() throws IOException {
            //given
            Long customerId = 1L;
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(
                    1L, 3, "ㅠㅠㅠㅠ"
            );

            given(commonService.findCustomerById(customerId)).willReturn(customer1);
            given(commonService.findStoreById(any(Long.class))).willReturn(store1);
            Review review = new Review(customer1, store1, requestDto);
            given(reviewRepository.save(any(Review.class))).willReturn(review);
            given(fileService.uploadFiles(review.getReviewId(), files, ImageEnum.REVIEW)).willReturn(null);
            given(fileRepository.findByItemIdAndImageEnum(review.getReviewId(), ImageEnum.REVIEW)).willReturn(null);

//            ReviewService reviewService = new ReviewService(reviewRepository, fileService, fileRepository, commonService);

            //when
            ReviewResponseDto result = reviewService.createReview(customerId, requestDto, files);

            //then
            assertNotNull(result);
            assertEquals(requestDto.getContent(), result.getContent());
        }

        @Test
        void 리뷰_생성_매장이_없을때() {
            Long customerId = 1L;
            ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(
                    3L, 3, "ㅠㅠㅠㅠ"
            );
            given(commonService.findStoreById(any(Long.class))).willThrow(new NotFoundException("해당하는 매장이 존재하지 않습니다."));

//            ReviewService reviewService = new ReviewService(reviewRepository, customerRepository, storeRepository, fileService, fileRepository);

            //when
             NotFoundException exception = assertThrows(NotFoundException.class, ()->reviewService.createReview(customerId, requestDto, files));

            //then
            assertEquals("해당하는 매장이 존재하지 않습니다.", exception.getMessage());

        }
    }

    @Nested
    class SelectReviewTest {
        @Test
        void 리뷰_다건_조회_성공() {
            //given
            int page = 1;
            int size = 10;
            Long storeId = 1L;
            int minStar = 1;
            int maxStar = 5;
            given(commonService.findStoreById(any(Long.class))).willReturn(store1);
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by("updatedAt").descending());
            given(reviewRepository.findAllByStoreAndStarBetween(store1, 1,5,pageable)).willReturn(new PageImpl<>(List.of(review1), pageable, size));
            //when
            Page<ReviewSimpleResponseDto> result = reviewService.getReviews(page,size,storeId,minStar,maxStar);
            //then
            assertEquals(1, result.getNumberOfElements());  //getTotalElements가 왜인지 10으로 나옴
            assertEquals(review1.getContent(), result.getContent().get(0).getContent());
        }
    }

    @Nested
    class reviewUpdateTest{
        @Test
        void 리뷰_수정_성공() throws IOException {
            //given
            Long reviewId = 1L;
            ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(5,"success");
            given(commonService.findCustomerById(anyLong())).willReturn(customer1);
            given(reviewRepository.findById(reviewId)).willReturn(Optional.ofNullable(review1));
            given(fileRepository.findByItemIdAndImageEnum(reviewId, ImageEnum.REVIEW)).willReturn(null);

            //when
            ReviewResponseDto result = reviewService.updateReview(reviewId, customer1.getCustomerId(), requestDto, files);

            //then
            assertEquals(requestDto.getContent(), result.getContent());

        }


        @Test
        void 리뷰_수정_해당_댓글_없음() {
            //given
            Long reviewId = 1L;
            ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(5,"success");
            given(commonService.findCustomerById(anyLong())).willReturn(customer1);
            given(reviewRepository.findById(reviewId)).willThrow(new NotFoundException("해당 댓글이 존재하지 않습니다."));

            //when
            NotFoundException exception = assertThrows(NotFoundException.class, ()->reviewService.updateReview(reviewId, customer1.getCustomerId(), requestDto, files));

            //then
            assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        void 리뷰_수정_작성자아님() {
            Long reviewId = 1L;
            ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(5,"success");
            given(commonService.findCustomerById(anyLong())).willReturn(customer2);
            given(reviewRepository.findById(reviewId)).willReturn(Optional.ofNullable(review1));

            //when
            DifferentUsersException exception = assertThrows(DifferentUsersException.class, ()->reviewService.updateReview(reviewId, customer2.getCustomerId(), requestDto, files));

            //then
            assertEquals("작성자가 아니므로 수정이 불가능합니다.", exception.getMessage());

        }
    }

    @Nested
    class ReviewDeleteTest {
        @Test
        void 리뷰_삭제_성공() {
            //given
            Long reviewId = 1L;
            given(commonService.findCustomerById(anyLong())).willReturn(customer1);
            given(reviewRepository.findById(reviewId)).willReturn(Optional.ofNullable(review1));
            given(fileRepository.findByItemIdAndImageEnum(reviewId, ImageEnum.REVIEW)).willReturn(new ArrayList<>());
            //when-
            reviewService.deleteReview(reviewId, customer1.getCustomerId());
            //then
            verify(reviewRepository, times(1)).deleteById(reviewId);
        }
    }

    @Test
    void 리뷰_삭제_해당_댓글_없음() {
        //given
        Long reviewId = 1L;
        given(commonService.findCustomerById(anyLong())).willReturn(customer1);
        given(reviewRepository.findById(reviewId)).willThrow(new NotFoundException("해당 댓글이 존재하지 않습니다."));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, ()->reviewService.deleteReview(reviewId, customer1.getCustomerId()));

        //then
        assertEquals("해당 댓글이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void 리뷰_삭제_작성자_다름 () {

        //given
        Long reviewId = 1L;
        given(commonService.findCustomerById(anyLong())).willReturn(customer2);
        given(reviewRepository.findById(reviewId)).willReturn(Optional.ofNullable(review1));

        //when
        DifferentUsersException exception = assertThrows(DifferentUsersException.class, ()->reviewService.deleteReview(reviewId, customer2.getCustomerId()));

        //then
        assertEquals("작성자가 아니므로 삭제가 불가능합니다.", exception.getMessage());
        verify(reviewRepository, times(0)).deleteById(reviewId);

    }}