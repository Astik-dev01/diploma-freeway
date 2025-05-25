package com.example.freeway.service.impl;

import com.example.freeway.db.entity.Meeting;
import com.example.freeway.db.entity.SysUser;
import com.example.freeway.db.enums.MeetingStatus;
import com.example.freeway.db.repository.MeetingRepository;
import com.example.freeway.db.repository.SysUserRepository;
import com.example.freeway.model.meeting.MeetingRequestDto;
import com.example.freeway.model.meeting.MeetingResponseDto;
import com.example.freeway.service.MeetingService;
import com.example.freeway.service.SysUserService;
import com.example.freeway.util.CustomMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final SysUserRepository sysUserRepository;
    private final SysUserService sysUserService;
    private final CustomMailSender customMailSender;


    @Override
    public List<MeetingResponseDto> getMyMeetings() {
        SysUser user = sysUserService.getFromContext();
        Long userId = user.getId();
        return meetingRepository.findAllByUserParticipation(userId).stream()
                .map(MeetingResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public MeetingResponseDto create(MeetingRequestDto dto) {
        SysUser currentUser = sysUserService.getFromContext();

        SysUser student = dto.getStudentId() != null
                ? sysUserRepository.findById(dto.getStudentId()).orElseThrow()
                : currentUser;

        SysUser teacher = dto.getTeacherId() != null
                ? sysUserRepository.findById(dto.getTeacherId()).orElseThrow()
                : currentUser;

        Meeting meeting = Meeting.builder()
                .student(student)
                .teacher(teacher)
                .createdBy(currentUser)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .topic(dto.getTopic())
                .status(MeetingStatus.PENDING)
                .build();
        meeting = meetingRepository.save(meeting);
        customMailSender.sendMeetingNotification(meeting, false, false);
        return MeetingResponseDto.from(meeting);
    }

    @Override
    public void approve(Long id, String comment) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow();
        meeting.setStatus(MeetingStatus.APPROVED);
        meeting.setComment(comment);
        meetingRepository.save(meeting);
        customMailSender.sendMeetingNotification(meeting, true, false);
    }

    @Override
    public void reject(Long id, String comment) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow();
        meeting.setStatus(MeetingStatus.REJECTED);
        meeting.setComment(comment);
        meetingRepository.save(meeting);
        customMailSender.sendMeetingNotification(meeting, false, true);
    }
}
