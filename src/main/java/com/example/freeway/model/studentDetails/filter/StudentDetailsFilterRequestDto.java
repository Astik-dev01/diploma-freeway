package com.example.freeway.model.studentDetails.filter;

import com.example.freeway.db.enums.StudentStatus;
import com.example.freeway.model.BasePageRequest;
import lombok.Data;

@Data
public class StudentDetailsFilterRequestDto  extends BasePageRequest {
    private String studentId;
    private StudentStatus status;
    private Long facultyId;
}
