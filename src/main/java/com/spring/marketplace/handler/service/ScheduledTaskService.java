package com.spring.marketplace.handler.service;

import com.spring.marketplace.database.repository.ProductRepository;
import com.spring.marketplace.utils.annotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.scheduling.enabled", matchIfMissing = false, havingValue = "true")
public class ScheduledTaskService {

    private final ProductRepository productRepository;

    @Transactional
    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @LogExecutionTime
    public void increaseProductPrice() {
        productRepository.updateAllProductsPrice();
        log.info("Increase product price by scheduled task");
    }
}
