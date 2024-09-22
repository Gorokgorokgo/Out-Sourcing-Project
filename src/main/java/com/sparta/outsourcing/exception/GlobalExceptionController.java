package com.sparta.outsourcing.exception;

import com.sparta.outsourcing.exception.customer.*;
import com.sparta.outsourcing.exception.store.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionController {

    //비밀번호 불일치
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> passwordMismatchException(PasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST + " :  " + ex.getMessage());
    }

    //데이터 못찾음
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> dataNotFoundException(DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpStatus.NOT_FOUND + " : " + ex.getMessage());
    }

    //중복 데이터 검사
    @ExceptionHandler(DataDuplicationException.class)
    public ResponseEntity<String> dataDuplicationException(DataDuplicationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(HttpStatus.CONFLICT + " : " + ex.getMessage());
    }

    //@Valid 할때 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessage.append(fieldName).append(": ").append(message).append(". ");
        });
        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }


    //입력으로 다른 타입 입력했을 때 에러 (ex . @PathVariable 이 Long 타입인데 String 타입 입력했을때)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String idTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ex.getName() + " 의 입력된 값은 잘못된 입력 입니다. " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName() + " 타입으로 " + " 정확히 입력해주세요."
                + " 당신이 넣은 값은 " + ex.getValue() + " 입니다.";
    }

    // 권한관련 exception
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String accessRights(ResponseStatusException ex) {
        return ex.getStatusCode() + ": " + ex.getReason();
    }

    @ExceptionHandler(WithdrawnMemberException.class)
    public ResponseEntity<String> withdrawnMember(WithdrawnMemberException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(HttpStatus.GONE + " : " + ex.getMessage());
    }

    @ExceptionHandler(DifferentUsersException.class)
    public ResponseEntity<String> differentUsers(DifferentUsersException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(HttpStatus.CONFLICT + " : " + ex.getMessage());
    }

    // ==== store ====
    // 사장님을 찾을 수 없는 예외 처리
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(HttpStatus.NOT_FOUND + " : " + ex.getMessage());
    }

    // 가게를 찾을 수 없는 예외 처리
    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<String> handleStoreNotFoundException(StoreNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(HttpStatus.NOT_FOUND + " : " + ex.getMessage());
    }

    // 권한이 없는 접근에 대한 예외 처리
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(HttpStatus.FORBIDDEN + " : " + ex.getMessage());
    }

    // 가게 개수 제한 초과 예외 처리
    @ExceptionHandler(MaxStoreLimitReachedException.class)
    public ResponseEntity<String> handleMaxStoreLimitReachedException(MaxStoreLimitReachedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(HttpStatus.BAD_REQUEST + " : " + ex.getMessage());
    }

    // 폐업 상태의 가게에 대한 조회 불가능 예외 처리
    @ExceptionHandler(StoreClosedException.class)
    public ResponseEntity<String> handleStoreClosedException(StoreClosedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(HttpStatus.BAD_REQUEST + " : " + ex.getMessage());
    }


}

