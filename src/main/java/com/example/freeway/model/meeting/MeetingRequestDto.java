package com.example.freeway.model.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRequestDto {
    Long teacherId;
    Long studentId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String topic;
}
