package com.spring.rateservice.controller;

import com.spring.rateservice.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rate")
@RequiredArgsConstructor
public class RateController {

    private final RateService rateService;

    @GetMapping
    public ResponseEntity<String> getRateValue(){
        return ResponseEntity.ok().body(rateService.getRateValue());
    }
}
