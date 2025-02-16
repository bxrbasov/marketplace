package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.User;
import com.spring.marketplace.handler.dto.UserUpdateDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateDtoToEntityConverter implements Converter<UserUpdateDto, User> {

    @Override
    public User convert(UserUpdateDto source) {
        return User.builder()
                .id(source.getId())
                .username(source.getUsername())
                .email(source.getEmail())
                .password(source.getPassword())
                .role(source.getRole())
                .build();
    }
}
