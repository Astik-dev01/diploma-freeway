package com.example.freeway.model.freeVisitApplication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Schema(description = "Запрос на создание заявки на свободное посещение")
public class FreeVisitApplicationRequestDto {

    Long studentId;

    @Schema(description = "Комментарий студента")
    String comment;

    private List<Long> teacherIds;


}
