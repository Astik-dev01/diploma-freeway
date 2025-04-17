package com.example.freeway.db.repository;

import com.example.freeway.db.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByStudentIdOrTeacherId(Long id, Long id1);
    @Query("SELECT m FROM Meeting m WHERE m.student.id = :userId OR m.teacher.id = :userId")
    List<Meeting> findAllByUserParticipation(@Param("userId") Long userId);
    List<Meeting> findAllByStudentIdOrTeacherId(Long currentUserId, Long currentUserId1);
}
