package com.example.freeway.service.impl;

import com.example.freeway.controller.BaseController;
import com.example.freeway.db.entity.ActionLog;
import com.example.freeway.db.enums.HttpMethodType;
import com.example.freeway.db.enums.ProcessObjectType;
import com.example.freeway.db.repository.ActionLogRepository;
import com.example.freeway.db.repository.specification.ActionLogSpecification;
import com.example.freeway.model.actionLog.filter.ActionLogFilter;
import com.example.freeway.model.actionLog.response.PageActionLogResponse;
import com.example.freeway.service.ActionLogService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl implements ActionLogService {

    private final ActionLogRepository repository;
    private final BaseController baseController;

    @Override
    public void create(
            HttpServletRequest request,
            ProcessObjectType objectType,
            HttpMethodType methodType,
            String details
    ) {
        ActionLog log = ActionLog.builder()
                .user(baseController.getUserFromContext())
                .objectType(objectType)
                .httpMethodType(methodType)
                .details(details)
                .remoteIp(baseController.tryDetectRemoteClientIp(request))
                .build();

        repository.save(log);
    }

    @Override
    public PageActionLogResponse getAll(ActionLogFilter filter) {
        Pageable pageable = PageRequest.of(BaseController.getPage(filter.getPage()), filter.getSize(),
                Sort.by(Sort.Direction.DESC,
                        "createdTime"));
        ActionLogSpecification specification = new ActionLogSpecification(filter);

        Page<ActionLog> page = repository.findAll(specification, pageable);

        return PageActionLogResponse.from(page);
    }

}
