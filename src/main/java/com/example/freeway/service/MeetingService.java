package com.example.freeway.service;

import com.example.freeway.model.meeting.MeetingRequestDto;
import com.example.freeway.model.meeting.MeetingResponseDto;

import java.util.List;

public interface MeetingService {
     MeetingResponseDto create(MeetingRequestDto dto);
    List<MeetingResponseDto> getMyMeetings();
     void approve(Long id, String comment);
     void reject(Long id, String comment);
}
