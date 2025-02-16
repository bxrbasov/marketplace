package com.spring.marketplace.handler.dto;

import lombok.*;

import java.math.BigInteger;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class MessageDto {
    private BigInteger chatId;
    private String content;
    private String owner;
    private Instant createdAt;
}
