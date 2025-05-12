package com.example.freeway.model.freeVisitApproval;

import com.example.freeway.db.entity.FreeVisitApproval;
import com.example.freeway.model.BasePageResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Schema(description = "Ответ страницы с согласованиями заявок на свободное посещение")
public class PageFreeVisitApprovalResponseDto extends BasePageResponse {

    @ArraySchema(schema = @Schema(implementation = FreeVisitApprovalResponseDto.class))
    List<FreeVisitApprovalResponseDto> content;

    public static PageFreeVisitApprovalResponseDto from(Page<FreeVisitApproval> page) {
        return PageFreeVisitApprovalResponseDto.builder()
                .content(page.getContent().stream()
                        .map(FreeVisitApprovalResponseDto::from)
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}
