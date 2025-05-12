package com.example.freeway.service.impl;

import com.example.freeway.controller.BaseController;
import com.example.freeway.db.entity.StudentDetails;
import com.example.freeway.db.repository.FacultyRepository;
import com.example.freeway.db.repository.StudentDetailsRepository;
import com.example.freeway.db.repository.SysUserRepository;
import com.example.freeway.db.repository.specification.StudentDetailsSpecification;
import com.example.freeway.exception.NotFoundException;
import com.example.freeway.model.studentDetails.filter.StudentDetailsFilterRequestDto;
import com.example.freeway.model.studentDetails.request.StudentDetailsRequestDto;
import com.example.freeway.model.studentDetails.response.PageStudentDetailsResponse;
import com.example.freeway.model.studentDetails.response.StudentDetailsResponseDto;
import com.example.freeway.service.StudentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudentDetailsServiceImpl implements StudentDetailsService {

    private final StudentDetailsRepository repository;
    private final SysUserRepository userRepository;
    private final FacultyRepository facultyRepository;

    @Override
    public StudentDetailsResponseDto create(StudentDetailsRequestDto dto) {
        StudentDetails entity = mapToEntity(dto);
        return StudentDetailsResponseDto.from(repository.save(entity));
    }

    @Override
    public StudentDetailsResponseDto update(Long id, StudentDetailsRequestDto dto) {
        StudentDetails existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Студент не найден"));
        StudentDetails updated = mapToEntity(dto);
        updated.setId(existing.getId());
        return StudentDetailsResponseDto.from(repository.save(updated));
    }

    @Override
    public PageStudentDetailsResponse getAll(StudentDetailsFilterRequestDto filter) {
        StudentDetailsSpecification specification = new StudentDetailsSpecification(filter);
        Pageable pageable = PageRequest.of(
                BaseController.getPage(filter.getPage()),
                filter.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<StudentDetails> page = repository.findAll(specification, pageable);
        return PageStudentDetailsResponse.from(page);
    }
    @Override
    public void delete(Long id) {
        StudentDetails entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Студент с ID " + id + " не найден"));

        repository.delete(entity);
    }

    @Override
    public StudentDetailsResponseDto getById(Long id) {
        return repository.findById(id)
                .map(StudentDetailsResponseDto::from)
                .orElseThrow(() -> new NotFoundException("Профиль студента не найден"));
    }

    private StudentDetails mapToEntity(StudentDetailsRequestDto dto) {
        return StudentDetails.builder()
                .studentId(dto.getStudentId())
                .user(userRepository.findById(dto.getUserId()).orElseThrow())
                .faculty(facultyRepository.findById(dto.getFacultyId()).orElseThrow())
                .advisor(userRepository.findById(dto.getAdvisorId()).orElseThrow())
                .status(dto.getStatus())
                .balance(dto.getBalance())
                .build();
    }
}
