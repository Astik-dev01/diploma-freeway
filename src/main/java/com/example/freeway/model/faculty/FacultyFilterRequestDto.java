package com.example.freeway.model.faculty;

import com.example.freeway.model.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Фильтр для факультетов")
public class FacultyFilterRequestDto extends BasePageRequest {
    private String name;
    private Boolean deleted;
}
