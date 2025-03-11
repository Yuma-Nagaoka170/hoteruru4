package com.example.moattravel.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.moattravel.entity.LoginLog;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    @Query("SELECT l.user.id, COUNT(l) FROM LoginLog l WHERE DATE(l.loginTime) = :date GROUP BY l.user.id")
    List<Object[]> countLoginsByDate(LocalDate date);
}

