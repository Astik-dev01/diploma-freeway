package com.example.freeway.service.impl;

import com.example.freeway.controller.BaseController;
import com.example.freeway.db.entity.*;
import com.example.freeway.db.enums.FreeVisitApprovalStatus;
import com.example.freeway.db.enums.FreeVisitStatus;
import com.example.freeway.db.repository.*;
import com.example.freeway.db.repository.specification.FreeVisitApplicationSpecification;
import com.example.freeway.exception.BadRequestException;
import com.example.freeway.exception.NotFoundException;
import com.example.freeway.model.freeVisitApplication.FreeVisitApplicationFilterRequestDto;
import com.example.freeway.model.freeVisitApplication.FreeVisitApplicationRequestDto;
import com.example.freeway.model.freeVisitApplication.FreeVisitApplicationResponseDto;
import com.example.freeway.model.freeVisitApplication.PageFreeVisitApplicationResponse;
import com.example.freeway.service.FreeVisitApplicationService;
import com.example.freeway.util.CustomMailSender;
import com.example.freeway.util.FileUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreeVisitApplicationServiceImpl implements FreeVisitApplicationService {

    private final FreeVisitApplicationRepository repository;
    private final StudentDetailsRepository studentDetailsRepository;
    private final FileUtils fileUtils;
    private final FreeVisitAttachmentRepository freeVisitAttachmentRepository;
    private final SysUserServiceImpl service;
    private final FreeVisitApprovalRepository freeVisitApprovalRepository;
    private final SysUserRepository sysUserRepository;
    private final FreeVisitApplicationRepository freeVisitApplicationRepository;
    private final CustomMailSender customMailSender;

    @Override
    @Transactional
    public FreeVisitApplicationResponseDto create(FreeVisitApplicationRequestDto dto, MultipartFile file) {
        SysUser user = service.getFromContext();
        StudentDetails student = studentDetailsRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Студент не найден"));

        if (dto.getTeacherIds() == null || dto.getTeacherIds().isEmpty()) {
            throw new BadRequestException("Выберите хотя бы одного преподавателя");
        }

        List<SysUser> selectedTeachers = sysUserRepository.findAllById(dto.getTeacherIds());

        boolean allAreTeachers = selectedTeachers.stream()
                .allMatch(u -> u.getRoles().stream()
                        .anyMatch(role -> role.getAlias().equalsIgnoreCase("TEACHER")));

        if (!allAreTeachers || selectedTeachers.size() != dto.getTeacherIds().size()) {
            throw new BadRequestException("Некоторые выбранные пользователи не являются преподавателями");
        }

        FreeVisitApplication entity = FreeVisitApplication.builder()
                .student(student)
                .comment(dto.getComment())
                .status(FreeVisitStatus.PENDING)
                .build();

        entity = repository.save(entity);

        String path = fileUtils.saveMultipartFileWithResize(file);
        FreeVisitAttachment attachment = FreeVisitAttachment.builder()
                .filePath(path)
                .application(entity)
                .build();
        freeVisitAttachmentRepository.save(attachment);
        entity.setDocument(attachment);
        entity = repository.save(entity);

        for (SysUser teacher : selectedTeachers) {
            FreeVisitApproval approval = FreeVisitApproval.builder()
                    .application(entity)
                    .teacher(teacher)
                    .status(FreeVisitApprovalStatus.PENDING)
                    .build();
            freeVisitApprovalRepository.save(approval);

            customMailSender.sendNewFreeVisitNotificationToTeacher(teacher, student);
        }

        return FreeVisitApplicationResponseDto.from(entity, fileUtils);
    }

    @Override
    public PageFreeVisitApplicationResponse getAll(FreeVisitApplicationFilterRequestDto filter) {
        Pageable pageable = PageRequest.of(BaseController.getPage(filter.getPage()), filter.getSize(),
                Sort.by(Sort.Direction.DESC, "id"));
        Page<FreeVisitApplication> page = repository.findAll(new FreeVisitApplicationSpecification(filter), pageable);
        return PageFreeVisitApplicationResponse.from(page, fileUtils);
    }
    @Override
    public List<FreeVisitApplicationResponseDto> getApplicationsForCurrentStudent() {
        SysUser user = service.getFromContext();

        StudentDetails student = studentDetailsRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Студент не найден"));

        List<FreeVisitApplication> apps = freeVisitApplicationRepository.findAllByStudentId(student.getId());

        return apps.stream()
                .map(app -> FreeVisitApplicationResponseDto.from(app, fileUtils))
                .collect(Collectors.toList());
    }
    @Override
    public FreeVisitApplicationResponseDto findById(Long id) {
        return FreeVisitApplicationResponseDto.from(
                repository.findById(id).orElseThrow(() -> new NotFoundException("Заявка не найдена")),
                fileUtils
        );
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
