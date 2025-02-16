package com.spring.marketplace.validation.imp;

import com.spring.marketplace.validation.UserUpdate;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserUpdateValidator implements ConstraintValidator<UserUpdate, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isBlank(value) || (StringUtils.isNotBlank(value) && value.length() >= 4);
    }
}
