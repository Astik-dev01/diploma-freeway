package com.example.freeway.service;

import com.example.freeway.db.entity.SysUser;
import com.example.freeway.model.user.filter.UserFilterDto;
import com.example.freeway.model.user.request.AdminChangePasswordRequestDto;
import com.example.freeway.model.user.request.ResetPasswordRequestDto;
import com.example.freeway.model.user.request.SysUserChangePasswordRequestDto;
import com.example.freeway.model.user.request.SysUserRequest;
import com.example.freeway.model.user.response.PageSysUserDtoResponse;
import com.example.freeway.model.user.response.SysUserResponseDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;


import java.util.Date;
import java.util.UUID;


public interface SysUserService {

    Date getStartOfDay();

    PageSysUserDtoResponse findAll(UserFilterDto filter);

    SysUser findById(Long id);

    SysUserResponseDto create(SysUserRequest userDto, HttpServletRequest request) throws Exception;

    SysUserResponseDto createUserByAdmin(SysUserRequest userDto, HttpServletRequest request) throws Exception;

    SysUserResponseDto update(SysUserRequest userDto, HttpServletRequest request);

    void delete(Long id, HttpServletRequest request);

    SysUserResponseDto changePassword(SysUserChangePasswordRequestDto userDto, HttpServletRequest request);

    SysUserResponseDto adminChangePassword(AdminChangePasswordRequestDto changePasswordRequestDto, HttpServletRequest request);


    SysUserResponseDto getByJWT(HttpServletRequest request);

    SysUser ban(Long userId, HttpServletRequest request);

    SysUser unban(Long userId, HttpServletRequest request);

    void updateTheNumberOfFailedLogins(String pin);

    SysUserResponseDto activateUser(UUID activationCode);

    void sendActivationLink(Long userId, HttpServletRequest request) throws MessagingException;

    void processForgotPassword(String email) throws MessagingException;

    void validateToken(String token);

    void resetPassword(ResetPasswordRequestDto requestDto);


    SysUser getFromContext();
}
