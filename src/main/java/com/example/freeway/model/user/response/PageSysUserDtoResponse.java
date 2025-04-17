package com.example.freeway.model.user.response;

import com.example.freeway.db.entity.SysUser;
import com.example.freeway.model.BasePageResponse;
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
@Schema(description = "Ответ с пагинацией для списка пользователей")
public class PageSysUserDtoResponse extends BasePageResponse {
    @Schema(description = "Контент (список пользователей)", implementation = SysUserResponseDto.class)
    private List<SysUserResponseDto> content;

    public static PageSysUserDtoResponse from(Page<SysUser> page) {
        return PageSysUserDtoResponse.builder()
                .content(page.getContent().stream()
                        .map(SysUserResponseDto::from)
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
