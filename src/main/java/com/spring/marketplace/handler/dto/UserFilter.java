package com.spring.marketplace.handler.dto;

import com.spring.marketplace.database.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilter {
    private BigInteger id;
    private String username;
    private String email;
    private Role role;
    private Instant createdAtFrom;
    private Instant createdAtTo;
}
