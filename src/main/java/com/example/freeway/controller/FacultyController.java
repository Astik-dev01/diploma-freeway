package com.example.freeway.controller;

import com.example.freeway.model.BaseResponse;
import com.example.freeway.model.faculty.FacultyFilterRequestDto;
import com.example.freeway.model.faculty.FacultyRequestDto;
import com.example.freeway.model.faculty.PageFacultyResponseDto;
import com.example.freeway.service.FacultyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
@Tag(name = "Факультеты", description = "Создание, обновление и получение данных о факультетах")
public class FacultyController {

    private final FacultyService service;

    @PostMapping("/get-all")
    @Operation(
            summary = "Получить список факультетов",
            responses = @ApiResponse(
                    description = "Список факультетов",
                    content = @Content(schema = @Schema(implementation = PageFacultyResponseDto.class))
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Фильтрация факультетов",
                    content = @Content(schema = @Schema(implementation = FacultyFilterRequestDto.class))
            )
    )
    public ResponseEntity<BaseResponse> getAll(@RequestBody FacultyFilterRequestDto filter) {
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
    @Operation(summary = "Получить факультет по ID")
    public ResponseEntity<BaseResponse> getById(@PathVariable Long id) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.findById(id))
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping
    @Operation(summary = "Создать факультет")
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody FacultyRequestDto dto) {
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
    @Operation(summary = "Обновить факультет")
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody FacultyRequestDto dto) {
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
    @Operation(summary = "Удалить факультет")
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
