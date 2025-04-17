package com.example.freeway.db.entity;

import com.example.freeway.db.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "student_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    SysUser user;

    @Column(name = "student_id", nullable = false, unique = true)
    String studentId;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = false)
    SysUser advisor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    StudentStatus status;

    @Column(name = "balance")
    BigDecimal balance;
}
