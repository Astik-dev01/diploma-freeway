package com.example.freeway.model.freeVisitApplication;

import com.example.freeway.db.entity.FreeVisitApplication;
import com.example.freeway.db.entity.StudentDetails;
import com.example.freeway.db.enums.FreeVisitStatus;
import com.example.freeway.model.attachment.FreeVisitAttachmentResponseDto;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalResponseDto;
import com.example.freeway.model.studentDetails.response.StudentDetailsResponseDto;
import com.example.freeway.model.user.response.SysUserResponseDto;
import com.example.freeway.util.FileUtils;
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
@Schema(description = "DTO для ответа по заявке на свободное посещение")
public class FreeVisitApplicationResponseDto {

    @Schema(description = "ID заявки", example = "1")
    Long id;

    @Schema(description = "Студент")
    StudentDetailsResponseDto student;

    @Schema(description = "Файл справки")
    FreeVisitAttachmentResponseDto document;

    @Schema(description = "Комментарий от студента", example = "Работаю полный день")
    String comment;

    @Schema(description = "Статус заявки", example = "PENDING")
    FreeVisitStatus status;

    @Schema(description = "Дата подачи заявки", example = "12.04.2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    String createdTime;

    @Schema(description = "Дата последнего обновления", example = "15.04.2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    String editedTime;

    @Schema(description = "Список согласований от преподавателей")
    List<FreeVisitApprovalResponseDto> approvals;

    public static FreeVisitApplicationResponseDto from(FreeVisitApplication entity, FileUtils fileUtils) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        return FreeVisitApplicationResponseDto.builder()
                .id(entity.getId())
                .student(StudentDetailsResponseDto.from(entity.getStudent()))
                .document(FreeVisitAttachmentResponseDto.builder()
                        .id(entity.getDocument().getId())
                        .url(fileUtils.buildUrl(entity.getDocument().getFilePath()))
                        .build())
                .comment(entity.getComment())
                .status(entity.getStatus())
                .approvals(entity.getApprovals() != null
                        ? entity.getApprovals().stream()
                        .map(FreeVisitApprovalResponseDto::from)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .createdTime(entity.getCreatedTime() != null ? dateFormat.format(entity.getCreatedTime()) : null)
                .editedTime(entity.getEditedTime() != null ? dateFormat.format(entity.getEditedTime()) : null)
                .build();
    }
}
