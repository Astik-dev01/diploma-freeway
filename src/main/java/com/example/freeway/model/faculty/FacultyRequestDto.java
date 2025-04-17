package com.example.freeway.model.faculty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO для создания/обновления факультета")
public class FacultyRequestDto {
    private Long id;

    @NotBlank(message = "Название факультета не может быть пустым")
    @Schema(description = "Название факультета")
    private String name;
}
