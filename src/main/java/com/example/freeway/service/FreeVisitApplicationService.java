package com.example.freeway.service;

import com.example.freeway.model.freeVisitApplication.FreeVisitApplicationFilterRequestDto;
import com.example.freeway.model.freeVisitApplication.FreeVisitApplicationRequestDto;
import com.example.freeway.model.freeVisitApplication.FreeVisitApplicationResponseDto;
import com.example.freeway.model.freeVisitApplication.PageFreeVisitApplicationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FreeVisitApplicationService {
    FreeVisitApplicationResponseDto create(FreeVisitApplicationRequestDto requestDto, MultipartFile file);

    PageFreeVisitApplicationResponse getAll(FreeVisitApplicationFilterRequestDto filter);

    FreeVisitApplicationResponseDto findById(Long id);

     List<FreeVisitApplicationResponseDto> getApplicationsForCurrentStudent();


        void delete(Long id);
}
