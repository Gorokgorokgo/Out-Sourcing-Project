package com.sparta.outsourcing.service.aspect;

import com.sparta.outsourcing.dto.order.OrderRequestDto;
import com.sparta.outsourcing.dto.customer.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class OrderLogAspect {

  // 주문 생성 메서드에 대한 포인트컷
  @Pointcut("execution(* com.sparta.outsourcing.service.OrderService.createOrder(..))")
  public void createOrder() {}

  // 주문 수정 메서드에 대한 포인트컷
  @Pointcut("execution(* com.sparta.outsourcing.service.OrderService.modifyOrder(..))")
  public void modifyOrder() {}

  // 주문 생성 및 수정 시 로깅
  @After("createOrder() || modifyOrder() && args(authUser, orderRequestDto, ..)")
  public void AfterOrderLog(JoinPoint joinPoint, AuthUser authUser, OrderRequestDto orderRequestDto) {
    // 요청 시각, 가게 ID, 주문 ID 로깅
    Long storeId = orderRequestDto.getStoreId();
    Long orderId = (Long) joinPoint.getArgs()[1]; // orderId 가져오기 (필요시 수정)

    log.info("Request time: {}, Store ID: {}, Order ID: {}",
        LocalDateTime.now(), storeId, orderId);
  }
}
