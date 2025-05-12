package com.example.freeway.model.role.filter;

import com.example.freeway.model.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Модель для фильтрации")
public class RoleFilter extends BasePageRequest {

    @Schema(description = "Доступные/Удаленные роли", example = "false")
    Boolean deleted = false;

//    @Schema(description = "Регистрация/Добавление", example = "true")
//    Boolean registration = false;

}
