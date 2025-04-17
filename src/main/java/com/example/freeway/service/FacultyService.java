package com.example.freeway.service;

import com.example.freeway.model.faculty.FacultyFilterRequestDto;
import com.example.freeway.model.faculty.FacultyRequestDto;
import com.example.freeway.model.faculty.FacultyResponseDto;
import com.example.freeway.model.faculty.PageFacultyResponseDto;

public interface FacultyService {
    FacultyResponseDto create(FacultyRequestDto dto);
    FacultyResponseDto update(Long id, FacultyRequestDto dto);
    void delete(Long id);
    FacultyResponseDto findById(Long id);
    PageFacultyResponseDto getAll(FacultyFilterRequestDto filter);
}
