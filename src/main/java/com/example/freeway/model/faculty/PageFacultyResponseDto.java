package com.example.freeway.model.faculty;

import com.example.freeway.db.entity.Faculty;
import com.example.freeway.model.BasePageResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Schema(description = "Страница факультетов")
public class PageFacultyResponseDto extends BasePageResponse {

    @ArraySchema(schema = @Schema(implementation = FacultyResponseDto.class))
    private List<FacultyResponseDto> content;

    public static PageFacultyResponseDto from(Page<Faculty> page) {
        return PageFacultyResponseDto.builder()
                .content(page.getContent().stream().map(FacultyResponseDto::from).toList())
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}
