package com.example.freeway.model.freeVisitApproval;

import com.example.freeway.db.enums.FreeVisitApprovalStatus;
import com.example.freeway.model.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Фильтр для получения заявок на согласование")
public class FreeVisitApprovalFilterRequestDto extends BasePageRequest {

    @Schema(description = "ID преподавателя")
    Long teacherId;

    @Schema(description = "ID заявки")
    Long applicationId;

    @Schema(description = "Статус согласования")
    FreeVisitApprovalStatus status;
}
