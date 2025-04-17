package com.example.freeway.controller;

import com.example.freeway.model.BaseResponse;
import com.example.freeway.model.studentDetails.filter.StudentDetailsFilterRequestDto;
import com.example.freeway.model.studentDetails.request.StudentDetailsRequestDto;
import com.example.freeway.model.studentDetails.response.PageStudentDetailsResponse;
import com.example.freeway.service.StudentDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-details")
@RequiredArgsConstructor
@Tag(name = "Студенты", description = "Создание, обновление и получение данных о студентах")
public class StudentDetailsController {

    private final StudentDetailsService service;

    @PostMapping("/get-all")
    @Operation(
            summary = "Получить список студентов",
            responses = @ApiResponse(
                    description = "Список студентов",
                    content = @Content(schema = @Schema(implementation = PageStudentDetailsResponse.class))
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Фильтрация студентов",
                    content = @Content(schema = @Schema(implementation = StudentDetailsFilterRequestDto.class))
            )
    )
    public ResponseEntity<BaseResponse> getAll(@RequestBody StudentDetailsFilterRequestDto filter) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getAll(filter))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить студента по ID")
    public ResponseEntity<BaseResponse> getById(@PathVariable Long id) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getById(id))
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping
    @Operation(summary = "Создать студента")
    public ResponseEntity<BaseResponse> create(@Validated @RequestBody StudentDetailsRequestDto dto) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.create(dto))
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping
    @Operation(summary = "Обновить студента")
    public ResponseEntity<BaseResponse> update(@Validated @RequestBody StudentDetailsRequestDto dto) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.update(dto.getId(), dto))
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить студента")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(id)
                        .build(),
                HttpStatus.OK
        );
    }
}
