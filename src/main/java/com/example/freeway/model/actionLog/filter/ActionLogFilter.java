package com.example.freeway.model.actionLog.filter;

import com.example.freeway.db.enums.HttpMethodType;
import com.example.freeway.db.enums.ProcessObjectType;
import com.example.freeway.model.BasePageRequest;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Модель для фильтрации")
public class ActionLogFilter extends BasePageRequest {

    @ArraySchema(schema =
    @Schema(description = "Идентификаторы пользователей", example = "1"))
    List<Long> userIds;

    @Schema(description = "Класс действия", example = "APPLICATION", implementation = ProcessObjectType.class)
    ProcessObjectType objectType;

    @Schema(description = "Тип действия", example = "GET", implementation = HttpMethodType.class)
    HttpMethodType methodType;

}
