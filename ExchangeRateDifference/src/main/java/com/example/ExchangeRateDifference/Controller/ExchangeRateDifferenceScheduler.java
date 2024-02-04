package com.example.ExchangeRateDifference.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.ExchangeRateDifference.Service.ExchangeRateDifferenceService;

import jakarta.annotation.PostConstruct;

@EnableScheduling
@Component
public class ExchangeRateDifferenceScheduler {
    
    @Autowired
    private ExchangeRateDifferenceService exchangeRateService;

    @PostConstruct
    public void init() {
        // This method will be called after the bean has been constructed and its dependencies have been injected
        retrieveAndStoreUsdExchangeRates();
    }

        // Runs the task at midnight regularly
        @Scheduled(cron = "0 0 0 * * *")
        public void retrieveAndStoreUsdExchangeRates() {
            LocalDate TodayDate = LocalDate.now();
            exchangeRateService.retrieveAndStoreUsdExchangeRates(TodayDate);
        }
}
