package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.User;
import com.spring.marketplace.handler.dto.UserReadDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToUserReadDtoConverter implements Converter<User, UserReadDto> {

    @Override
    public UserReadDto convert(User source) {
        return UserReadDto.builder()
                .id(source.getId())
                .username(source.getUsername())
                .email(source.getEmail())
                .createdAt(source.getCreatedAt())
                .role(source.getRole())
                .build();
    }
}
