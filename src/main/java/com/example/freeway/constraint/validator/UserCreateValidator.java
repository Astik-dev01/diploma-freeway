package com.example.freeway.constraint.validator;

import com.example.freeway.constraint.ValidUser;
import com.example.freeway.db.entity.SysRole;
import com.example.freeway.db.entity.SysUser;
import com.example.freeway.db.repository.SysRoleRepository;
import com.example.freeway.db.repository.SysUserRepository;
import com.example.freeway.model.user.request.SysUserRequest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.regex.Pattern;


@RequiredArgsConstructor
public class UserCreateValidator implements ConstraintValidator<ValidUser, SysUserRequest> {

    private final SysUserRepository repository;
    private final SysRoleRepository roleRepository;


    @Override
    public void initialize(ValidUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SysUserRequest value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean isValid = true;


        // Проверка телефона
        String phoneNumber = value.getPhone();
        if (phoneNumber == null || phoneNumber.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("error.valid.phoneNumber.is_blank")
                    .addPropertyNode("phone")
                    .addConstraintViolation();
            return false;
        }

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        try {
            Phonenumber.PhoneNumber parsed = phoneUtil.parse(phoneNumber, null);

            if (!phoneUtil.isValidNumber(parsed)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("error.phone.invalid_number")
                        .addPropertyNode("phone")
                        .addConstraintViolation();
                return false;
            }

            String normalizedPhone = phoneUtil.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164);
            value.setPhone(normalizedPhone);

            var userByPhone = repository.findByPhoneNumber(normalizedPhone);
            if (userByPhone.isPresent() && (value.getId() == null || !userByPhone.get().getId().equals(value.getId()))) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("error.valid.user.phone.exists")
                        .addPropertyNode("phone")
                        .addConstraintViolation();
                return false;
            }

        } catch (NumberParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("error.phone.invalid_format")
                    .addPropertyNode("phone")
                    .addConstraintViolation();
            return false;
        }

        // Проверка email
        String email = value.getEmail();
        String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

        if (email == null || email.isBlank()) {
            context.buildConstraintViolationWithTemplate("error.valid.email.not_null")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

        if (!Pattern.matches(emailPattern, email)) {
            context.buildConstraintViolationWithTemplate("error.valid.email.format")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

        Optional<SysUser> userByEmail = repository.findByEmail(email);
        if (userByEmail.isPresent() && (value.getId() == null || !userByEmail.get().getId().equals(value.getId()))) {
            context.buildConstraintViolationWithTemplate("error.valid.user.email")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

        // Проверка пароля
        if (value.getPassword() == null || value.getPassword().isBlank()) {
            context.buildConstraintViolationWithTemplate("error.valid.password")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        var roles = roleRepository.findAllById(value.getRoleIds());
        if (roles.size() != value.getRoleIds().size()) {
            context.buildConstraintViolationWithTemplate("error.valid.user.roles")
                    .addPropertyNode("roles")
                    .addConstraintViolation();
            return false;
        }

        int passwordLength = roles.stream()
                .mapToInt(SysRole::getPasswordLength)
                .max()
                .orElse(8); // по умолчанию минимальная длина 8

        String dynamicPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{" + passwordLength + ",}$";
        if (!value.getPassword().matches(dynamicPattern)) {
            context.buildConstraintViolationWithTemplate("error.valid.password")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        return isValid;
    }
}
