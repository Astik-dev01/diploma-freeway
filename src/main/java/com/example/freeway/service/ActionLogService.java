package com.example.freeway.service;

import com.example.freeway.db.enums.HttpMethodType;
import com.example.freeway.db.enums.ProcessObjectType;
import com.example.freeway.model.actionLog.filter.ActionLogFilter;
import com.example.freeway.model.actionLog.response.PageActionLogResponse;
import jakarta.servlet.http.HttpServletRequest;


public interface ActionLogService {
    void create(
            HttpServletRequest request,
            ProcessObjectType objectType,
            HttpMethodType methodType,
            String details
    );

    PageActionLogResponse getAll(ActionLogFilter filter);
}
