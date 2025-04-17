package com.example.freeway.service;

import com.example.freeway.model.freeVisitApplication.PageFreeVisitApplicationResponse;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalFilterRequestDto;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalRequestDto;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalResponseDto;
import com.example.freeway.model.freeVisitApproval.PageFreeVisitApprovalResponseDto;
import org.springframework.data.domain.Pageable;

public interface FreeVisitApprovalService {
    PageFreeVisitApprovalResponseDto getAll(FreeVisitApprovalFilterRequestDto filter);

    FreeVisitApprovalResponseDto approve(FreeVisitApprovalRequestDto dto);

    PageFreeVisitApplicationResponse getApplicationsForCurrentTeacher(Pageable pageable);
}
