package com.example.moattravel.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.moattravel.service.DailyLoginBatchService;

@RestController
@RequestMapping("/batch")
public class BatchController {

    private final DailyLoginBatchService dailyLoginBatchService;

    public BatchController(DailyLoginBatchService dailyLoginBatchService) {
        this.dailyLoginBatchService = dailyLoginBatchService;
    }

    @PostMapping("/daily-logins")
    public String runDailyLoginBatch() {
        dailyLoginBatchService.processDailyLoginCounts();
        return "Daily login batch executed manually";
    }
}
