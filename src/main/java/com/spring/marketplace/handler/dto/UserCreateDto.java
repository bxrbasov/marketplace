package com.spring.marketplace.handler.dto;

import com.spring.marketplace.database.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {

    private BigInteger id;

    @NotBlank(message = "Username must be not empty")
    @Size(min = 4, message = "Username length min 4 chars")
    private String username;

    @NotBlank(message = "Email must be not empty")
    @Email
    private String email;

    @NotBlank(message = "Password must be not empty")
    @Size(min = 4, message = "Password length min 4 chars")
    private String password;

    private Role role;
}
