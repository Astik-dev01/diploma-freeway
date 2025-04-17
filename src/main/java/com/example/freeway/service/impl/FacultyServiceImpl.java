package com.example.freeway.service.impl;

import com.example.freeway.controller.BaseController;
import com.example.freeway.db.entity.Faculty;
import com.example.freeway.db.repository.FacultyRepository;
import com.example.freeway.db.repository.specification.FacultySpecification;
import com.example.freeway.exception.NotFoundException;
import com.example.freeway.model.faculty.FacultyFilterRequestDto;
import com.example.freeway.model.faculty.FacultyRequestDto;
import com.example.freeway.model.faculty.FacultyResponseDto;
import com.example.freeway.model.faculty.PageFacultyResponseDto;
import com.example.freeway.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;

    @Override
    public FacultyResponseDto create(FacultyRequestDto dto) {
        Faculty faculty = Faculty.builder().name(dto.getName()).build();
        return FacultyResponseDto.from(repository.save(faculty));
    }

    @Override
    public FacultyResponseDto update(Long id, FacultyRequestDto dto) {
        Faculty faculty = repository.findById(id).orElseThrow(() -> new NotFoundException("Факультет не найден"));
        faculty.setName(dto.getName());
        return FacultyResponseDto.from(repository.save(faculty));
    }

    @Override
    public void delete(Long id) {
        Faculty faculty = repository.findById(id).orElseThrow(() -> new NotFoundException("Факультет не найден"));
        faculty.setDeleted(true);
        repository.save(faculty);
    }

    @Override
    public FacultyResponseDto findById(Long id) {
        Faculty faculty = repository.findById(id).orElseThrow(() -> new NotFoundException("Факультет не найден"));
        return FacultyResponseDto.from(faculty);
    }

    @Override
    public PageFacultyResponseDto getAll(FacultyFilterRequestDto filter) {
        Pageable pageable = PageRequest.of(BaseController.getPage(filter.getPage()), filter.getSize());
        Page<Faculty> page = repository.findAll(new FacultySpecification(filter), pageable);
        return PageFacultyResponseDto.from(page);
    }
}
