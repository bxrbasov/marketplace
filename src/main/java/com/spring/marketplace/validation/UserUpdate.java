package com.spring.marketplace.validation;

import com.spring.marketplace.validation.imp.UserUpdateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserUpdateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UserUpdate {

    String message() default "Password min size 4 or empty";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
