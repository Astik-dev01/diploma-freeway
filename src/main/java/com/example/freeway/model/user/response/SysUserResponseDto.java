package com.example.freeway.model.user.response;

import com.example.freeway.db.entity.SysUser;
import com.example.freeway.db.enums.Gender;
import com.example.freeway.model.role.response.SysRoleResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@Schema(description = "DTO для ответа пользователя")
public class SysUserResponseDto {

    @Schema(description = "Идентификатор пользователя", example = "1")
    Long id;

    @Schema(description = "Флаг блокировки пользователя")
    Boolean isBanned;

    @Schema(description = "Фамилия", example = "Тестов")
    String secondName;

    @Schema(description = "Имя", example = "Тест")
    String name;

    @Schema(description = "Пол")
    Gender gender;

    @Schema(description = "Дата рождения", example = "1990-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date birthdate;

    @Schema(description = "Последнее время авторизации", example = "2024-11-18 14:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    Date lastLogin;

    @Schema(description = "Удален", example = "false")
    Boolean deleted;

    @Schema(description = "Требуется смена пароля при следующей авторизации", example = "false")
    Boolean passwordChangeNextLogon;

    @Schema(description = "Время последнего изменения пароля", example = "2024-11-18 14:30:00")
    Date passwordLastChangeTime;

    @Schema(description = "Временный доступ до времени", example = "2024-11-18 14:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    Date temporaryAccessUntilTime;

    @Schema(description = "Время редактирования", example = "2024-11-18 14:30:00")
    Date editedTime;

    @Schema(description = "Время создания записи", example = "2024-11-18 14:30:00")
    Date createdTime;

    @Schema(description = "Список ролей", implementation = SysRoleResponseDto.class)
    List<SysRoleResponseDto> roles;

    @Schema(description = "Код страны", example = "+996")
    String countryCode;

    @Schema(description = "Номер телефона")
    String phone;

    @Schema(description = "Email")
    String email;

    @Schema(description = "Email подтверждён")
    Boolean emailVerified;

    @Schema(description = "Телефон подтверждён")
    Boolean phoneNumberVerified;



    public static SysUserResponseDto from(SysUser user) {
        if (user == null) {
            return null;
        }

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String fullPhone = user.getPhoneNumber();

        String countryCode;
        String nationalNumber;

        try {
            Phonenumber.PhoneNumber parsed = phoneUtil.parse(fullPhone, null);
            countryCode = "+" + parsed.getCountryCode();
            nationalNumber = String.valueOf(parsed.getNationalNumber());
        } catch (NumberParseException e) {
            countryCode = null;
            nationalNumber = null;
        }
        List<SysRoleResponseDto> roleResponses = user.getRoles() != null ? user.getRoles().stream()
                .map(SysRoleResponseDto::from)
                .collect(Collectors.toList()) : Collections.emptyList();

        return SysUserResponseDto.builder()
                .id(user.getId())
                .phone(nationalNumber)
                .countryCode(countryCode)
                .isBanned(user.isBanned())
                .emailVerified(user.isEmailVerified())
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .secondName(user.getSecondName())
                .name(user.getName())
                .gender(user.getGender() != null ? user.getGender() : null)
                .birthdate(user.getBirthdate())
                .lastLogin(user.getLastLogin())
                .deleted(user.isDeleted())
                .passwordChangeNextLogon(user.getPasswordChangeNextLogon())
                .passwordLastChangeTime(user.getPasswordLastChangeTime())
                .temporaryAccessUntilTime(user.getTemporaryAccessUntilTime())
                .editedTime(user.getEditedTime())
                .createdTime(user.getCreatedTime())
                .roles(roleResponses)
                .phoneNumberVerified(user.getPhoneNumberVerified())
                .build();
    }
}
