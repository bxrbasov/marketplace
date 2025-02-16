package com.spring.marketplace.handler.dto;

import lombok.*;

import java.math.BigInteger;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteDto {
    private String username;
    private UUID productId;
}
