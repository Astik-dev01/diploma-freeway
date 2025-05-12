package com.example.freeway.model.freeVisitApproval;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Запрос на подтверждение заявки преподавателем")
public class FreeVisitApprovalRequestDto {

    @NotNull
    @Schema(description = "ID заявки", example = "5")
    Long applicationId;

    @NotNull
    @Schema(description = "Согласие преподавателя", example = "true")
    Boolean approved;

    @Schema(description = "Комментарий преподавателя", example = "Студент посещает занятия регулярно")
    String comment;
}
