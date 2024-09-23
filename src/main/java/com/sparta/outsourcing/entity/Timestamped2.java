package com.sparta.outsourcing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped2 {
    @CreatedDate
    @Column(updatable = false) // 업데이트 안되게 만들어야 함.
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt; // createdAt 만든 시각.
}
