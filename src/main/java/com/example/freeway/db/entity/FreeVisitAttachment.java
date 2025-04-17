package com.example.freeway.db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "free_visit_attachments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FreeVisitAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id", nullable = false)
    FreeVisitApplication application;

    @Column(name = "file_path", nullable = false)
    String filePath;

    @Column(name = "content_type")
    String contentType;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;
}
