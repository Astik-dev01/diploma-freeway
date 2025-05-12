package com.example.freeway.constraint.validator;

import com.example.freeway.constraint.ValidLoginUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginUsernameValidator implements ConstraintValidator<ValidLoginUsername, String> {
    @Override
    public void initialize(ValidLoginUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
}
