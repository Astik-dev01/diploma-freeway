package com.example.freeway.db.entity;

import com.example.freeway.db.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sys_users")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;


    @Column(name = "second_name", nullable = false)
    String secondName;

    @Column(name = "name", nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "birthdate")
    Date birthdate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sys_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<SysRole> roles = new HashSet<>();

    @Column(name = "last_password")
    String lastPassword;

    @Column(name = "second_last_password")
    String secondLastPassword;

    @Column(name = "third_last_password")
    String thirdLastPassword;

    @Builder.Default
    @Column(name = "failed_login_attempts")
    Integer failedLoginAttempts = 0;

    @Column(name = "last_login")
    Date lastLogin;

    @Builder.Default
    @Column(name = "password_change_next_logon") //Сменить пароль при следующей авторизации
    Boolean passwordChangeNextLogon = true;

    @Column(name = "password_last_change_time") //Время последнего изменения пароля
    @Temporal(TemporalType.TIMESTAMP)
    Date passwordLastChangeTime = new Date();

    @Column(name = "temporary_access_until_time") //Временный доступ до времени
    @Temporal(TemporalType.TIMESTAMP)
    Date temporaryAccessUntilTime;

    @Builder.Default
    @Column(name = "is_banned")
    boolean isBanned = false;

    @Builder.Default
    @Column(name = "email_verified")
    boolean emailVerified = false;

    @Column(name = "email", nullable = false)
    @Email
    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Builder.Default
    @Column(name = "phone_number_verified")
    Boolean phoneNumberVerified = false;

    @Builder.Default
    @Column(name = "deleted")
    boolean deleted = false;

    @Column(name = "edited_time")
    Date editedTime;

    @CreationTimestamp
    @Column(name = "created_time", updatable = false, nullable = false)
    Date createdTime;


}
