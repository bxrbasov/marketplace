package com.spring.rateservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RateService {

    @Value("${app.rate.value}")
    private String value;

    public String getRateValue() {
        log.info("Get rate value from the microservice: rate-service");
        return value;
    }
}
