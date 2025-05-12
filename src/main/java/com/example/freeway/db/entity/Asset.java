package com.example.freeway.db.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

//@Entity
//@Table(name = "fa_assets")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    SysUser user;


    @Column(name = "deleted")
    boolean deleted = false;

    @Column(name = "edited_time")
    Date editedTime;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;


}
