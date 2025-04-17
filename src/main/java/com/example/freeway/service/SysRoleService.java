package com.example.freeway.service;

import com.example.freeway.db.entity.SysRole;
import com.example.freeway.model.role.filter.RoleFilter;
import com.example.freeway.model.role.request.SysRoleRequest;
import com.example.freeway.model.role.response.PageSysRoleResponse;
import com.example.freeway.model.role.response.SysRoleResponseDto;
import jakarta.servlet.http.HttpServletRequest;



public interface SysRoleService {

    SysRole findById(Long id);

    SysRoleResponseDto create(SysRoleRequest roleDto, HttpServletRequest request);

    SysRoleResponseDto update(SysRoleRequest roleDto, HttpServletRequest request);

    void delete(Long id, HttpServletRequest request);

    PageSysRoleResponse getAll(RoleFilter filter);
}
