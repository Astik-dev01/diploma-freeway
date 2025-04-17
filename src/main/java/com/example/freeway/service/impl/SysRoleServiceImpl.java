package com.example.freeway.service.impl;

import com.example.freeway.controller.BaseController;
import com.example.freeway.db.entity.SysRole;
import com.example.freeway.db.repository.SysRoleRepository;
import com.example.freeway.db.repository.specification.RoleSpecification;
import com.example.freeway.exception.NotFoundException;
import com.example.freeway.model.role.filter.RoleFilter;
import com.example.freeway.model.role.request.SysRoleRequest;
import com.example.freeway.model.role.response.PageSysRoleResponse;
import com.example.freeway.model.role.response.SysRoleResponseDto;
import com.example.freeway.service.SysLogRequestService;
import com.example.freeway.service.SysRoleService;
import com.example.freeway.util.GsonConfig;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {
    private final SysRoleRepository repository;
    private final SysLogRequestService logService;
    private final Gson gson = GsonConfig.createGson();


    @Override
    public SysRole findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("error.role.not_found"));
    }

    @Override
    public SysRoleResponseDto create(SysRoleRequest roleDto, HttpServletRequest request) {
        Optional<SysRole> byAlias = repository.findByAlias(roleDto.getAlias());
        SysRole sysRole;
        if (byAlias.isPresent()) {
            sysRole = byAlias.get();
            sysRole.setDeleted(false);
            sysRole.setEditedTime(new Date());
        } else {
            sysRole = SysRole.builder()
                    .alias(roleDto.getAlias())
                    .nameRu(roleDto.getNameRu())
                    .nameKy(roleDto.getNameKy())
                    .build();
        }

        var result = repository.save(sysRole);
        logService.saveSuccessToDb(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), gson.toJson(roleDto), request);
        return SysRoleResponseDto.from(result);
    }

    @Override
    public SysRoleResponseDto update(SysRoleRequest roleDto, HttpServletRequest request) {
        SysRole role = findById(roleDto.getId());

        role.setAlias(roleDto.getAlias());
        role.setNameRu(roleDto.getNameRu());
        role.setNameKy(roleDto.getNameKy());
        role.setDeleted(false);
        role.setEditedTime(new Date());
        role.setCreatedTime(role.getCreatedTime());

        var result = repository.save(role);
        logService.saveSuccessToDb(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(roleDto), request);
        return SysRoleResponseDto.from(result);
    }

    @Override
    public void delete(Long id, HttpServletRequest request) {
        var role = findById(id);
        role.setDeleted(true);
        repository.save(role);
        logService.saveSuccessToDb(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(id), request);
    }

    @Override
    public PageSysRoleResponse getAll(RoleFilter filter) {
        RoleSpecification specification = new RoleSpecification(filter);
        Pageable pageable = PageRequest.of(BaseController.getPage(filter.getPage()), filter.getSize(),
                Sort.by(Sort.Direction.DESC, "id"));
        Page<SysRole> page = repository.findAll(specification, pageable);
        return PageSysRoleResponse.from(page);
    }
}
