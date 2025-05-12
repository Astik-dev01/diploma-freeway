package com.example.freeway.model.freeVisitApplication;

import com.example.freeway.db.entity.FreeVisitApplication;
import com.example.freeway.model.BasePageResponse;
import com.example.freeway.util.FileUtils;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ страницы заявок на свободное посещение")
public class PageFreeVisitApplicationResponse extends BasePageResponse {

    @ArraySchema(schema = @Schema(implementation = FreeVisitApplicationResponseDto.class))
    List<FreeVisitApplicationResponseDto> content;

    public static PageFreeVisitApplicationResponse from(Page<FreeVisitApplication> page, FileUtils fileUtils) {
        return PageFreeVisitApplicationResponse.builder()
                .content(page.getContent().stream()
                        .map(app -> FreeVisitApplicationResponseDto.from(app, fileUtils))
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}
