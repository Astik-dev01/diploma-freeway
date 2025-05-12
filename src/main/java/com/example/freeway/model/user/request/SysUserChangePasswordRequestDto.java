package com.example.freeway.model.user.request;

import com.example.freeway.constraint.ChangePassword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для изменения пароля пользователя")
@ChangePassword
public class SysUserChangePasswordRequestDto {

    @Schema(description = "Персональный идентификационный номер (PIN)", example = "22808200200873")
    String email;

    @Schema(description = "Новый пароль пользователя", example = "Password123!")
    String password;

    @Schema(description = "Старый пароль (Не нужен для супер админа)", example = "Password123!")
    String oldPassword;
}
