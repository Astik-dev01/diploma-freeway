package com.example.freeway.db.entity;

import com.example.freeway.db.enums.FreeVisitApprovalStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "free_visit_approvals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FreeVisitApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id")
    FreeVisitApplication application;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    SysUser teacher;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    FreeVisitApprovalStatus status;

    @Column(columnDefinition = "TEXT")
    String comment;

    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    Date createdTime;

    @UpdateTimestamp
    @Column(name = "edited_time")
    Date editedTime;
}
