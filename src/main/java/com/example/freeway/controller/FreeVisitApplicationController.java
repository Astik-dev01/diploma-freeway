package com.example.freeway.controller;

import com.example.freeway.model.BaseResponse;
import com.example.freeway.model.freeVisitApplication.FreeVisitApplicationFilterRequestDto;
import com.example.freeway.model.freeVisitApplication.FreeVisitApplicationRequestDto;
import com.example.freeway.service.FreeVisitApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/free-visit")
@RequiredArgsConstructor
@Tag(name = "Заявки на свободное посещение", description = "Управление заявками на свободное посещение")
public class FreeVisitApplicationController {

    private final FreeVisitApplicationService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Создание заявки")
    public ResponseEntity<BaseResponse> create(
            @RequestPart("data") @Valid FreeVisitApplicationRequestDto dto,
            @RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.create(dto, file))
                        .build(),
                HttpStatus.OK
        );
    }
    @GetMapping("/my")
    @Operation(summary = "Список заявок текущего студента")
    public ResponseEntity<BaseResponse> getMyApplications() {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getApplicationsForCurrentStudent())
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/get-all")
    @Operation(summary = "Получение всех заявок", requestBody =
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = FreeVisitApplicationFilterRequestDto.class))
    ))
    public ResponseEntity<BaseResponse> getAll(@RequestBody FreeVisitApplicationFilterRequestDto filter) {
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
    @Operation(summary = "Получить заявку по ID")
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


    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заявку")
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
