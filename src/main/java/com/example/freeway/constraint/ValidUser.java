package com.example.freeway.constraint;


import com.example.freeway.constraint.validator.UserCreateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserCreateValidator.class)
public @interface ValidUser {

    String message() default "Invalid PIN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
