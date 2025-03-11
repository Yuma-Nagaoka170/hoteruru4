package com.example.moattravel.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.moattravel.entity.DailyLoginCount;
import com.example.moattravel.repository.DailyLoginCountRepository;
import com.example.moattravel.repository.LoginLogRepository;

@Service
public class DailyLoginBatchService {

    private static final Logger logger = LoggerFactory.getLogger(DailyLoginBatchService.class);
    private final LoginLogRepository loginLogRepository;
    private final DailyLoginCountRepository dailyLoginCountRepository;

    public DailyLoginBatchService(LoginLogRepository loginLogRepository, DailyLoginCountRepository dailyLoginCountRepository) {
        this.loginLogRepository = loginLogRepository;
        this.dailyLoginCountRepository = dailyLoginCountRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // 毎日深夜0時に実行
    @Transactional
    public void processDailyLoginCounts() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        try {
            List<Object[]> loginCounts = loginLogRepository.countLoginsByDate(yesterday);
            
            for (Object[] result : loginCounts) {
                Long userId = (Long) result[0];
                Long count = (Long) result[1];
                dailyLoginCountRepository.save(new DailyLoginCount(userId, yesterday, count));
            }
            logger.info("Daily login count batch completed for {}", yesterday);
        } catch (Exception e) {
            logger.error("Error processing daily login counts for {}: {}", yesterday, e.getMessage(), e);
        }
    }
}

