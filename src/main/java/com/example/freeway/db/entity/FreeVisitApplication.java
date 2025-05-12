package com.example.freeway.db.entity;

import com.example.freeway.db.enums.FreeVisitStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "free_visit_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FreeVisitApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    StudentDetails student;

    @Column(name = "comment", columnDefinition = "TEXT")
    String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    FreeVisitStatus status;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    FreeVisitAttachment document;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<FreeVisitApproval> approvals = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    Date createdTime;

    @UpdateTimestamp
    @Column(name = "edited_time")
    Date editedTime;
}
