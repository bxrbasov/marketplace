package com.spring.marketplace.handler.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.marketplace.client.RateServiceClient;
import com.spring.marketplace.utils.exception.ApplicationException;
import com.spring.marketplace.utils.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {

    @Value("${app.json.path}")
    private String pathToJson;
    @Value("${spring.redis.time-to-live}")
    private long redisTtl;
    private final RateServiceClient rateServiceClient;
    private final JedisPool jedisPool;


    public BigDecimal convertPrice(BigDecimal from) {
        try (FileReader fileReader = new FileReader(pathToJson)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Double> prices = objectMapper.readValue(fileReader, new TypeReference<>(){});
            double exchangeRate = prices.get("exchangeRate");

            log.info("Successfully converted currency");
            return from.divide(new BigDecimal(exchangeRate), 2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ApplicationException(ErrorType.FAILED_TO_CONVERT_CURRENCY);
        }
    }

    public BigDecimal convertPriceWithCache(BigDecimal object) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists("exchangeRate")) {
                log.info("Get exchange rate from cache");
                return object.divide(new BigDecimal(jedis.get("exchangeRate")), 2, BigDecimal.ROUND_HALF_UP);
            }

            String exchangeRate = rateServiceClient.getRateValue();
            log.info("call rate service getRateValue() method");
            jedis.setex("exchangeRate", redisTtl, exchangeRate);

            return object.divide(new BigDecimal(exchangeRate), 2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return convertPrice(object);
        }
    }
}
