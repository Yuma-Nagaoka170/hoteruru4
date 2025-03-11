package com.example.moattravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.moattravel.entity.DailyLoginCount;

@Repository
public interface DailyLoginCountRepository extends JpaRepository<DailyLoginCount, Long> {
}

