package com.example.freeway.model.attachment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Документ заявки")
public class FreeVisitAttachmentResponseDto {
    @Schema(description = "ID фото")
    Long id;

    @Schema(description = "URL миниатюры (170x126)")
    String url;
}
