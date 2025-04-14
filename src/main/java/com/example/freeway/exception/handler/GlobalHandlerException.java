package com.example.freeway.exception.handler;

import com.example.freeway.controller.BaseController;
import com.example.freeway.db.repository.ErrorMessageRepository;
import com.example.freeway.exception.*;
import com.example.freeway.model.BaseResponse;
import com.example.freeway.model.errorMessage.ErrorDto;
import com.example.freeway.service.SysLogRequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalHandlerException {
    private final SysLogRequestService logService;
    private final ErrorMessageRepository errorMessageRepository;

    @ExceptionHandler(EmailSendException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse> handleEmailSendException(EmailSendException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<BaseResponse> handleNotFoundException(NotFoundException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<BaseResponse> handleBadCredentialException(BadCredentialException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<BaseResponse> handleApiException(ApplicationException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );

        return responseEntity(
                e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD,
                HttpStatus.valueOf(e.getStatusCode())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse> handleMethodNotValidException(MethodArgumentNotValidException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );

        List<ErrorDto> fieldValidErrors = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String rawMessage = error.getDefaultMessage();

                    if (rawMessage != null && rawMessage.startsWith("{") && rawMessage.endsWith("}")) {
                        String content = rawMessage.substring(1, rawMessage.length() - 1);
                        String[] parts = content.split(",", 2);
                        String code = parts[0];
                        String[] args = parts.length > 1 ? parts[1].split(",") : new String[0];

                        var msg = errorMessageRepository.findById(code)
                                .orElseGet(() -> errorMessageRepository.findById("error.validation.default").get());

                        return ErrorDto.builder()
                                .field(resolveField(error))
                                .ru(MessageFormat.format(msg.getMessageRu(), (Object[]) args))
                                .kg(MessageFormat.format(msg.getMessageKg(), (Object[]) args))
                                .eng(MessageFormat.format(msg.getMessageEng(), (Object[]) args))
                                .build();
                    }

                    var msg = errorMessageRepository.findById(rawMessage)
                            .orElseGet(() -> errorMessageRepository.findById("error.validation.default").get());

                    return ErrorDto.builder()
                            .field(resolveField(error))
                            .ru(msg.getMessageRu())
                            .kg(msg.getMessageKg())
                            .eng(msg.getMessageEng())
                            .build();
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(BaseResponse.builder()
                .success(BaseController.Constants.ERROR)
                .msg("Validation failed")
                .res(fieldValidErrors)
                .build(), HttpStatus.BAD_REQUEST);
    }

    private String resolveField(ObjectError error) {
        if (error instanceof FieldError fe) {
            return fe.getField();
        } else {
            return "default";
        }
    }


    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse> handleBadRequestException(BadRequestException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse> handleEntityExistsException(EntityExistsException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<BaseResponse> handleIOException(IOException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<BaseResponse> handleNoHandlerFound(NoHandlerFoundException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<BaseResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Обработка всех остальных исключений
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<BaseResponse> handleAllExceptions(Exception e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BaseResponse> handleValidationException(ValidationException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest httpServletRequest) {
        logService.saveExceptionToFileAndDb(
                e.getStackTrace()[0].getClassName(),
                e,
                e.getStackTrace()[0].getMethodName(),
                httpServletRequest
        );
        return responseEntity(e.getMessage() != null ? e.getMessage() : BaseController.Constants.UNDEFINED_FIELD, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<BaseResponse> responseEntity(String exceptionMessage, HttpStatus httpStatus) {
        var error = errorMessageRepository.findById(exceptionMessage);
        List<Map<String, String>> errorDetails = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put(String.valueOf(httpStatus.value()), exceptionMessage);
        errorDetails.add(map);

        return new ResponseEntity<>(BaseResponse.builder()
                .success(BaseController.Constants.ERROR)
                .msg(BaseController.Constants.RestResponseMessages.BAD_REQUEST.name())
                .res(error.isPresent() ?
                        ErrorDto.from(error.get()) : errorDetails)
                .build(),
                httpStatus);
    }
}
