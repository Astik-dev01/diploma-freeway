package com.example.freeway.model.studentDetails.response;

import com.example.freeway.db.entity.StudentDetails;
import com.example.freeway.db.enums.StudentStatus;
import com.example.freeway.model.faculty.FacultyResponseDto;
import com.example.freeway.model.user.response.SysUserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
@Schema(description = "DTO для отображения сведений о студенте")
public class StudentDetailsResponseDto {

    private Long id;

    @Schema(description = "Пользователь (студент)")
    private SysUserResponseDto user;

    private String studentId;

    @Schema(description = "Информация о факультете")
    private FacultyResponseDto faculty;

    @Schema(description = "Куратор студента")
    private SysUserResponseDto advisor;

    private StudentStatus status;

    private BigDecimal balance;

    public static StudentDetailsResponseDto from(StudentDetails entity) {
        return StudentDetailsResponseDto.builder()
                .id(entity.getId())
                .user(SysUserResponseDto.from(entity.getUser()))
                .studentId(entity.getStudentId())
                .faculty(FacultyResponseDto.from(entity.getFaculty()))
                .advisor(SysUserResponseDto.from(entity.getAdvisor()))
                .status(entity.getStatus())
                .balance(entity.getBalance())
                .build();
    }
}
