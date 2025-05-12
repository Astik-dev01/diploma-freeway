package com.example.freeway.controller;


import com.example.freeway.model.BaseResponse;
import com.example.freeway.model.user.filter.UserFilterDto;
import com.example.freeway.model.user.request.*;
import com.example.freeway.model.user.response.PageSysUserDtoResponse;
import com.example.freeway.model.user.response.SysUserResponseDto;
import com.example.freeway.query_limiter.QueryLimited;
import com.example.freeway.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Создание, обновление, удаление и получение")
public class SysUserController {

    private final SysUserService service;

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить пользователя по ID",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    ResponseEntity<BaseResponse> findById(@Parameter(description = "ID Пользователя", required = true) @PathVariable Long id) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(SysUserResponseDto.from(service.findById(id)))
                        .build(), HttpStatus.OK
        );
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Запрос на восстановление пароля")
    public ResponseEntity<BaseResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto requestDto) throws MessagingException {
        service.processForgotPassword(requestDto.getEmail());
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/reset-password/{token}")
    @Operation(summary = "Проверка токена сброса пароля")
    public ResponseEntity<BaseResponse> checkToken(@PathVariable String token) {
        service.validateToken(token);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Сброс пароля")
    public ResponseEntity<BaseResponse> resetPassword(@RequestBody @Valid ResetPasswordRequestDto requestDto) {
        service.resetPassword(requestDto);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/create")
    @Operation(summary = "Создание нового пользователя",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для создания нового пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserRequest.class))
            ))
    ResponseEntity<BaseResponse> create(@Validated @RequestBody SysUserRequest userDto, HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.create(userDto, request))
                        .build(), HttpStatus.OK
        );

    }

    @PostMapping("/admin/create")
    @Operation(summary = "Создание нового пользователя",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для создания нового пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserRequest.class))
            ))
    ResponseEntity<BaseResponse> createUserByAdmin(@Validated @RequestBody SysUserRequest userDto, HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.createUserByAdmin(userDto, request))
                        .build(), HttpStatus.OK
        );

    }

    @PutMapping
    @Operation(summary = "Обновление пользователя",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для обновления данных пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserRequest.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt")))
    ResponseEntity<BaseResponse> update(@Validated @RequestBody SysUserRequest userDto, HttpServletRequest request) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.update(userDto, request))
                        .build(), HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить пользователя по ID",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = Long.class))),
            parameters =
            @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    ResponseEntity<BaseResponse> delete(@Parameter(description = "ID пользователя", required = true) @PathVariable Long id, HttpServletRequest request) {
        service.delete(id, request);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(id)
                        .build(), HttpStatus.OK
        );
    }

    @PostMapping("/change-password")
    @Operation(summary = "Обновление пароля пользователя",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для смены пароля пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserChangePasswordRequestDto.class))
            ),
            parameters =
            @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    ResponseEntity<BaseResponse> changeUserPassword(@Validated @RequestBody SysUserChangePasswordRequestDto userDto, HttpServletRequest request) {

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.changePassword(userDto, request))
                        .build(), HttpStatus.OK
        );
    }

    @PostMapping("/admin/change-password")
    @Operation(summary = "Обновление пароля пользователя Супер Администратором",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для смены пароля пользователя",
                    content = @Content(schema = @Schema(implementation = AdminChangePasswordRequestDto.class))
            ),
            parameters =
            @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    public ResponseEntity<BaseResponse> changePasswordAnotherUser(
            @Validated @RequestBody AdminChangePasswordRequestDto changePasswordRequestDto,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.adminChangePassword(changePasswordRequestDto, request))
                        .build(), HttpStatus.OK
        );

    }

    @GetMapping("/by-jwt")
    @Operation(
            summary = "Получение данных пользователя по JWT",
            responses = @ApiResponse(
                    description = "Успешный ответ с данными пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt")
            )
    )
    ResponseEntity<BaseResponse> getByJWT(HttpServletRequest request) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getByJWT(request))
                        .build(), HttpStatus.OK
        );
    }

    @PostMapping("/get-all")
    @Operation(summary = "Получить список доступных пользователей",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = PageSysUserDtoResponse.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO для фильтрации - необязательное",
                    content = @Content(schema = @Schema(implementation = UserFilterDto.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt")))
    public ResponseEntity<BaseResponse> findActiveUsers(
            @Parameter(description = "DTO для фильтрации - необязательное") @RequestBody(required = false) UserFilterDto filter) {

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.findAll(filter))
                        .build(), HttpStatus.OK
        );
    }

    @QueryLimited
    @PostMapping("/activate")
    @Operation(summary = "Активация пользователя",
            responses = @ApiResponse(content = @Content(schema =
            @Schema(implementation = SysUserResponseDto.class))))
    ResponseEntity<BaseResponse> activateUser(@RequestParam(name = "activationCode") UUID activationCode) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg("The user was successfully activated")
                        .res(service.activateUser(activationCode))
                        .build(), HttpStatus.OK
        );

    }

}
