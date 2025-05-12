package com.example.freeway.service.impl;


import com.example.freeway.db.entity.SysLogAuthorization;
import com.example.freeway.db.entity.SysUser;
import com.example.freeway.db.repository.SysLogAuthorizationRepository;
import com.example.freeway.service.SysLogAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysLogsAuthorizationServiceImpl implements SysLogAuthorizationService {

    private final SysLogAuthorizationRepository repository;

    @Override
    public void saveSuccessfulAuth(SysUser user, String ip) {
        repository.save(new SysLogAuthorization(user, ip));
    }
}
