package com.spring.marketplace.handler.dto;

import com.spring.marketplace.database.model.Role;
import lombok.*;

import java.math.BigInteger;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class UserReadDto {
    private BigInteger id;
    private String username;
    private String email;
    private Role role;
    private Instant createdAt;
}
