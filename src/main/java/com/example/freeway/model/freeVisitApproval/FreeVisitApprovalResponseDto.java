package com.example.freeway.model.freeVisitApproval;

import com.example.freeway.db.entity.FreeVisitApproval;
import com.example.freeway.db.enums.FreeVisitApprovalStatus;
import com.example.freeway.model.user.response.SysUserResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для отображения статуса согласования заявки преподавателем")
public class FreeVisitApprovalResponseDto {

    @Schema(description = "ID согласования")
    Long id;

    @Schema(description = "Преподаватель")
    SysUserResponseDto teacher;

    @Schema(description = "Статус согласования", example = "APPROVED")
    FreeVisitApprovalStatus status;

    @Schema(description = "Комментарий преподавателя")
    String comment;

    @Schema(description = "Дата согласования", example = "16.04.2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    String createdTime;

    public static FreeVisitApprovalResponseDto from(FreeVisitApproval entity) {
        return FreeVisitApprovalResponseDto.builder()
                .id(entity.getId())
                .teacher(SysUserResponseDto.from(entity.getTeacher()))
                .status(entity.getStatus())
                .comment(entity.getComment())
                .createdTime(entity.getCreatedTime() != null
                        ? new SimpleDateFormat("dd.MM.yyyy").format(entity.getCreatedTime())
                        : null)
                .build();
    }
}
