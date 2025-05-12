package com.example.freeway.service.impl;

import com.example.freeway.controller.BaseController;
import com.example.freeway.db.entity.FreeVisitApplication;
import com.example.freeway.db.entity.FreeVisitApproval;
import com.example.freeway.db.entity.SysUser;
import com.example.freeway.db.enums.FreeVisitApprovalStatus;
import com.example.freeway.db.enums.FreeVisitStatus;
import com.example.freeway.db.repository.FreeVisitApplicationRepository;
import com.example.freeway.db.repository.FreeVisitApprovalRepository;
import com.example.freeway.db.repository.SysUserRepository;
import com.example.freeway.db.repository.specification.FreeVisitApprovalSpecification;
import com.example.freeway.exception.BadRequestException;
import com.example.freeway.exception.NotFoundException;
import com.example.freeway.model.freeVisitApplication.PageFreeVisitApplicationResponse;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalFilterRequestDto;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalRequestDto;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalResponseDto;
import com.example.freeway.model.freeVisitApproval.PageFreeVisitApprovalResponseDto;
import com.example.freeway.service.FreeVisitApprovalService;
import com.example.freeway.util.FileUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreeVisitApprovalServiceImpl implements FreeVisitApprovalService {

    private final FreeVisitApprovalRepository repository;
    private final SysUserServiceImpl userService; // или SysUserService
    private final FreeVisitApplicationRepository applicationRepository;
    private final SysUserRepository sysUserRepository;
    private final FileUtils fileUtils;


    @Override
    public PageFreeVisitApprovalResponseDto getAll(FreeVisitApprovalFilterRequestDto filter) {
        Pageable pageable = PageRequest.of(
                BaseController.getPage(filter.getPage()),
                filter.getSize(),
                Sort.by(Sort.Direction.DESC, "createdTime")
        );

        Specification<FreeVisitApproval> spec = new FreeVisitApprovalSpecification(filter);
        Page<FreeVisitApproval> page = repository.findAll(spec, pageable);
        return PageFreeVisitApprovalResponseDto.from(page);
    }

    @Override
    public PageFreeVisitApplicationResponse getApplicationsForCurrentTeacher(Pageable pageable) {
        SysUser teacher = userService.getFromContext();

        // Получаем все согласования преподавателя
        Page<FreeVisitApproval> approvals = repository.findAllByTeacherId(teacher.getId(), pageable);

        // Достаём связанные заявки
        List<FreeVisitApplication> applications = approvals.getContent().stream()
                .map(FreeVisitApproval::getApplication)
                .collect(Collectors.toList());

        Page<FreeVisitApplication> page = new PageImpl<>(applications, pageable, approvals.getTotalElements());

        return PageFreeVisitApplicationResponse.from(page, fileUtils);
    }

    @Override
    @Transactional
    public FreeVisitApprovalResponseDto approve(FreeVisitApprovalRequestDto dto) {
        SysUser teacher = userService.getFromContext();

        FreeVisitApplication application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new NotFoundException("Заявка не найдена"));

        Optional<FreeVisitApproval> existing = repository.findByApplicationIdAndTeacherId(dto.getApplicationId(), teacher.getId());

        FreeVisitApproval approval;

        if (existing.isPresent()) {
            approval = existing.get();
            if (approval.getStatus() != FreeVisitApprovalStatus.PENDING) {
                throw new BadRequestException("Вы уже проголосовали по этой заявке");
            }
            // обновляем статус и комментарий
            approval.setStatus(dto.getApproved() ? FreeVisitApprovalStatus.APPROVED : FreeVisitApprovalStatus.REJECTED);
            approval.setComment(dto.getComment());
        } else {
            approval = FreeVisitApproval.builder()
                    .application(application)
                    .teacher(teacher)
                    .status(dto.getApproved() ? FreeVisitApprovalStatus.APPROVED : FreeVisitApprovalStatus.REJECTED)
                    .comment(dto.getComment())
                    .build();
        }

        repository.save(approval); // сохраняем либо обновлённую, либо новую

        // проверка: все ли проголосовали
        List<SysUser> allTeachers = sysUserRepository.getAllByRolesAlias("TEACHER");
        List<FreeVisitApproval> allApprovals = repository.findAllByApplicationId(dto.getApplicationId());

        Set<Long> votedTeacherIds = allApprovals.stream()
                .filter(a -> a.getStatus() != FreeVisitApprovalStatus.PENDING)
                .map(a -> a.getTeacher().getId())
                .collect(Collectors.toSet());

        boolean allVoted = allApprovals.stream()
                .filter(a -> a.getStatus() != FreeVisitApprovalStatus.PENDING)
                .map(a -> a.getTeacher().getId())
                .collect(Collectors.toSet())
                .containsAll(
                        allTeachers.stream().map(SysUser::getId).collect(Collectors.toSet())
                );

        if (allVoted) {
            boolean allApproved = allApprovals.stream()
                    .filter(a -> a.getStatus() != FreeVisitApprovalStatus.PENDING)
                    .allMatch(a -> a.getStatus() == FreeVisitApprovalStatus.APPROVED);

            application.setStatus(allApproved
                    ? FreeVisitStatus.APPROVED_BY_TEACHERS
                    : FreeVisitStatus.REJECTED_BY_TEACHERS
            );
            applicationRepository.save(application);
        }

        return FreeVisitApprovalResponseDto.from(approval);
    }
}
