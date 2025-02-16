package com.spring.marketplace.handler.dto;

import lombok.*;

import java.math.BigInteger;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatCreateDto {
    private String name;
    private BigInteger owner;
    private String buyer;
    private UUID topic;
}
