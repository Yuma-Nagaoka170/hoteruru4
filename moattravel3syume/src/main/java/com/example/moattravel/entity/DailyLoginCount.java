package com.example.moattravel.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "daily_login_counts")
public class DailyLoginCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDate loginDate;

    private Long count;

    public DailyLoginCount() {}

    public DailyLoginCount(Long userId, LocalDate loginDate, Long count) {
        this.userId = userId;
        this.loginDate = loginDate;
        this.count = count;
    }

    // ゲッター・セッター
}

