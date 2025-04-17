package com.example.freeway.controller;

import com.example.freeway.model.BaseResponse;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalFilterRequestDto;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalRequestDto;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalResponseDto;
import com.example.freeway.model.freeVisitApproval.PageFreeVisitApprovalResponseDto;
import com.example.freeway.service.FreeVisitApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approval")
@RequiredArgsConstructor
@Tag(name = "Согласование заявок", description = "Работа с согласованиями преподавателей")
public class FreeVisitApprovalController {

    private final FreeVisitApprovalService service;

    @PostMapping("/get-all")
    @Operation(
            summary = "Получить список согласований заявок преподавателей",
            responses = @ApiResponse(
                    description = "Список согласований",
                    content = @Content(schema = @Schema(implementation = PageFreeVisitApprovalResponseDto.class))
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Фильтрация по заявкам и преподавателям",
                    content = @Content(schema = @Schema(implementation = FreeVisitApprovalFilterRequestDto.class))
            )
    )
    public ResponseEntity<BaseResponse> getAll(@RequestBody FreeVisitApprovalFilterRequestDto filter) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getAll(filter))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/my-applications")
    @Operation(summary = "Заявки, ожидающие одобрения от текущего преподавателя")
    public ResponseEntity<BaseResponse> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getApplicationsForCurrentTeacher(pageable))
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping
    @Operation(
            summary = "Согласовать заявку",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Запрос на подтверждение заявки",
                    content = @Content(schema = @Schema(implementation = FreeVisitApprovalRequestDto.class))
            ),
            responses = @ApiResponse(
                    description = "Ответ по согласованию",
                    content = @Content(schema = @Schema(implementation = FreeVisitApprovalResponseDto.class))
            )
    )
    public ResponseEntity<BaseResponse> approve(@RequestBody @Valid FreeVisitApprovalRequestDto dto) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.approve(dto))
                        .build(),
                HttpStatus.OK
        );
    }
}
