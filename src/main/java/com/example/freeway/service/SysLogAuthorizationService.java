package com.example.freeway.service;


import com.example.freeway.db.entity.SysUser;

public interface SysLogAuthorizationService {

    void saveSuccessfulAuth(SysUser user, String ip);

}
