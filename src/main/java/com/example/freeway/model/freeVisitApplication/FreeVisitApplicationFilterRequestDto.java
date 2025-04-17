package com.example.freeway.model.freeVisitApplication;

import com.example.freeway.db.enums.FreeVisitStatus;
import com.example.freeway.model.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Фильтр заявок на свободное посещение")
public class FreeVisitApplicationFilterRequestDto extends BasePageRequest {

    @Schema(description = "ID студента")
    Long studentId;

    @Schema(description = "Статус заявки")
    FreeVisitStatus status;
}
