package com.example.freeway.model.user.request;

import com.example.freeway.constraint.FutureDate;
import com.example.freeway.constraint.ValidUser;
import com.example.freeway.db.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "Модель создания пользователя")
@ValidUser
public class SysUserRequest {

    @Schema(description = "Идентификатор пользователя")
    Long id;

    @NotBlank(message = "error.valid.last.name.not_null")
    String secondName;

    @NotBlank(message = "error.valid.name.not_null")
    String name;

    @Schema(description = "Пол", implementation = Gender.class)
    Gender gender;

    @Schema(description = "Пароль пользователя", example = "Password123!")
    String password;

    @Schema(description = "Дата рождения")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    Date birthdate;

    @FutureDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    Date temporaryAccessUntilTime;

    @Schema(description = "Список идентификаторов ролей", example = "[1, 2]")
    List<Long> roleIds;

    @Schema(description = "Адрес электронной почты пользователя", example = "ivanov@example.com")
    String email;

    @Schema(description = "Номер телефона пользователя", example = "+996111222333")
    String phone;

    @Schema(description = "Номер студенческого билета (только для студентов)")
    String studentId;

    @Schema(description = "ID факультета (только для студентов)")
    Long facultyId;

    @Schema(description = "ID куратора/наставника (только для студентов)")
    Long advisorId;

    @Schema(description = "OTP код подтверждения почты")
    private String otpCode;

}
