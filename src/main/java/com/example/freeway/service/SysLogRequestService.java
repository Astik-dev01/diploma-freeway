package com.example.freeway.service;

import com.example.freeway.db.entity.SysLogRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface SysLogRequestService {
    void save(SysLogRequest request);

    void saveSuccessToDb(
            String className,
            String methodName,
            String message,
            HttpServletRequest httpServletRequest
    );

    void saveSuccessToFileAndDb(
            String nameClass,
            String nameFunction,
            String message,
            HttpServletRequest request
    );

    void saveExceptionToFileAndDb(
            String nameClass,
            Exception e,
            String nameFunction,
            HttpServletRequest httpServletRequest
    );

    void saveErrorToFileAndDb(
            String nameClass,
            String errorMessage,
            String nameFunction,
            HttpServletRequest httpServletRequest
    );
}
