package com.spring.marketplace.handler.dto;

import com.spring.marketplace.database.model.Role;
import com.spring.marketplace.validation.UserUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigInteger;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class UserUpdateDto {

    private BigInteger id;

    @NotBlank(message = "Username must be not empty")
    @Size(min = 4, message = "Username length min 4 chars")
    private String username;

    @NotBlank(message = "Email must be not empty")
    @Email
    private String email;

    @UserUpdate
    private String password;

    private Instant createdAt;

    private Role role;
}
