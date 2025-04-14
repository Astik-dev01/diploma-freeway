package com.example.freeway.service;


import com.example.freeway.db.entity.sys.SysUser;

public interface SysLogAuthorizationService {

    void saveSuccessfulAuth(SysUser user, String ip);

}
