package com.example.freeway.db.repository;


import com.example.freeway.db.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {
    @Query("SELECT u FROM SysUser u where  u.email = :login and u.deleted is false")
    Optional<SysUser> findByLoginAndActive(String login);

    Optional<SysUser> findByIdAndDeletedFalse(Long id);

    Optional<SysUser> findByEmail(String username);

    boolean existsByEmail(String email);

    Optional<SysUser> findByRolesAlias(String role);


    Optional<SysUser> findByPhoneNumber(String phoneNumber);

    List<SysUser> getAllByRolesAlias(String teacher);
}
