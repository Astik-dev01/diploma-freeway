package com.example.freeway.model.faculty;

import com.example.freeway.db.entity.Faculty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO для ответа по факультету")
public class FacultyResponseDto {
    private Long id;
    private String name;

    public static FacultyResponseDto from(Faculty faculty) {
        return FacultyResponseDto.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .build();
    }
}
