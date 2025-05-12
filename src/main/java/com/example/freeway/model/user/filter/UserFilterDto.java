package com.example.freeway.model.user.filter;

import com.example.freeway.model.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO для фильтрации пользователей")
public class UserFilterDto extends BasePageRequest {

    @Schema(description = "Полное имя пользователя", example = "Иванов Иван Иванович")
    String fullName;

    @Schema(description = "Номер телефона", example = "+996123456789")
    String phoneNumber;

    @Schema(description = "Список идентификаторов ролей", example = "[1, 2, 3]")
    List<Long> rolesIds;

    @Schema(description = "Удален?", example = "false")
    Boolean deleted = false;

    @Schema(description = "Заблокирован?", example = "false")
    Boolean isBanned = false;

}
