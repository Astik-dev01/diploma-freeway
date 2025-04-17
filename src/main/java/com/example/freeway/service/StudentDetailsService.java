package com.example.freeway.service;

import com.example.freeway.model.studentDetails.filter.StudentDetailsFilterRequestDto;
import com.example.freeway.model.studentDetails.request.StudentDetailsRequestDto;
import com.example.freeway.model.studentDetails.response.PageStudentDetailsResponse;
import com.example.freeway.model.studentDetails.response.StudentDetailsResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;


public interface StudentDetailsService {
    StudentDetailsResponseDto create(StudentDetailsRequestDto dto);
    StudentDetailsResponseDto update(Long id, StudentDetailsRequestDto dto);
     PageStudentDetailsResponse getAll(StudentDetailsFilterRequestDto filter);
    StudentDetailsResponseDto getById(Long id);
    void delete(Long id);

}
