package com.example.freeway.model.meeting;

import com.example.freeway.db.entity.Meeting;
import com.example.freeway.db.enums.MeetingStatus;
import com.example.freeway.model.user.response.SysUserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingResponseDto {
    Long id;
    SysUserResponseDto student;
    SysUserResponseDto teacher;
    SysUserResponseDto createdBy;
    LocalDateTime startTime;
    LocalDateTime endTime;
    MeetingStatus status;
    String topic;
    String comment;              // ✅ новое поле
    Date createdTime;   // ✅ новое поле

    public static MeetingResponseDto from(Meeting entity) {
        return MeetingResponseDto.builder()
                .id(entity.getId())
                .student(SysUserResponseDto.from(entity.getStudent()))
                .teacher(SysUserResponseDto.from(entity.getTeacher()))
                .createdBy(SysUserResponseDto.from(entity.getCreatedBy())) // 👈
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .status(entity.getStatus())
                .topic(entity.getTopic())
                .comment(entity.getComment())
                .createdTime(entity.getCreatedTime())
                .build();
    }
}
