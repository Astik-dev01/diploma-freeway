package com.example.freeway.controller;

import com.example.freeway.model.BaseResponse;
import com.example.freeway.model.meeting.MeetingRequestDto;
import com.example.freeway.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
@Tag(name = "Встречи", description = "Создание, просмотр и управление встречами")
public class MeetingController {

    private final MeetingService service;

    @PostMapping("/create")
    @Operation(summary = "Создать встречу")
    public ResponseEntity<BaseResponse> create(@RequestBody(description = "Создание встречи",
            required = true,
            content = @Content(schema = @Schema(implementation = MeetingRequestDto.class)))
                                               @org.springframework.web.bind.annotation.RequestBody MeetingRequestDto dto) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.create(dto))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/my")
    @Operation(summary = "Получить мои встречи (для студента или преподавателя)")
    public ResponseEntity<BaseResponse> getMyMeetings() {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getMyMeetings())
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Подтвердить встречу")
    public ResponseEntity<BaseResponse> approve(@PathVariable Long id,
                                                @RequestParam(required = false) String comment) {
        service.approve(id, comment);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Отклонить встречу")
    public ResponseEntity<BaseResponse> reject(@PathVariable Long id,
                                               @RequestParam(required = false) String comment) {
        service.reject(id, comment);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }
    }
