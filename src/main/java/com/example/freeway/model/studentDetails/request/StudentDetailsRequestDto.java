package com.example.freeway.model.studentDetails.request;

import com.example.freeway.db.enums.StudentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO для создания или обновления сведений о студенте")
public class StudentDetailsRequestDto {
    private Long id;

    @Schema(description = "ID пользователя (SysUser)")
    @NotNull
    private Long userId;

    @Schema(description = "Уникальный номер студента")
    @NotBlank
    private String studentId;

    @Schema(description = "ID факультета")
    @NotNull
    private Long facultyId;

    @Schema(description = "ID куратора (SysUser)")
    @NotNull
    private Long advisorId;

}

