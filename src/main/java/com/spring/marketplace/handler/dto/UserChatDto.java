package com.spring.marketplace.handler.dto;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChatDto {
    private String username;
    private BigInteger chatId;
}
