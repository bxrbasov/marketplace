package com.spring.marketplace.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatReadDto {
    private BigInteger id;
    private ProductDto product;
    private List<UserReadDto> users;
    private List<MessageDto> messages;
}
