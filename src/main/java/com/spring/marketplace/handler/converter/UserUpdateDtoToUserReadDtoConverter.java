package com.spring.marketplace.handler.converter;

import com.spring.marketplace.handler.dto.UserReadDto;
import com.spring.marketplace.handler.dto.UserUpdateDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateDtoToUserReadDtoConverter implements Converter<UserUpdateDto, UserReadDto> {

    @Override
    public UserReadDto convert(UserUpdateDto source) {
        return UserReadDto.builder()
                .id(source.getId())
                .username(source.getUsername())
                .email(source.getEmail())
                .role(source.getRole())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
