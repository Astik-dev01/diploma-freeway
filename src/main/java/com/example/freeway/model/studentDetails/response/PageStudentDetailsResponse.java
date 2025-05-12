package com.example.freeway.model.studentDetails.response;

import com.example.freeway.db.entity.StudentDetails;
import com.example.freeway.model.BasePageResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Schema(description = "Ответ страницы с деталями студентов")
public class PageStudentDetailsResponse extends BasePageResponse {

    @ArraySchema(schema =
    @Schema(description = "Список студентов", implementation = StudentDetailsResponseDto.class))
    List<StudentDetailsResponseDto> content;

    public static PageStudentDetailsResponse from(Page<StudentDetails> page) {
        return PageStudentDetailsResponse.builder()
                .content(page.getContent().stream()
                        .map(StudentDetailsResponseDto::from)
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}
