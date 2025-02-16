package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.User;
import com.spring.marketplace.handler.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserCreateDtoToEntityConverter implements Converter<UserCreateDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User convert(UserCreateDto source) {
        return User.builder()
                .username(source.getUsername())
                .password(passwordEncoder.encode(source.getPassword()))
                .email(source.getEmail())
                .role(source.getRole())
                .build();
    }
}
