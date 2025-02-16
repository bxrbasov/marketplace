package com.spring.marketplace.advice;

import com.spring.marketplace.handler.dto.ProductDto;
import com.spring.marketplace.handler.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ProductGlobalControllerAdvice implements ResponseBodyAdvice<ProductDto> {

    private final ExchangeService exchangeService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ProductDto.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public ProductDto beforeBodyWrite(ProductDto body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Optional.of(body).ifPresentOrElse((item) -> {
            body.setPrice(exchangeService.convertPriceWithCache(body.getPrice()));
            log.info("ProductGlobalControllerAdvice worked successfully");
        }, () -> {
            log.error("ProductGlobalControllerAdvice failed");
            throw new NullPointerException("ProductDto body return null");
        });

        return body;
    }
}
