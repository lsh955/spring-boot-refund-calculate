package com.example.project.app.account.service;

import com.example.project.app.account.dto.UserDto;
import com.example.project.app.common.dto.JwtTokenDto;
import com.example.project.app.common.enums.AccountStatus;

/**
 * @author 이승환
 * @since 2022-03-14
 */
public interface AccountService {

    AccountStatus addSignup(String userId, String password, String name, String regNo) throws Exception;

    JwtTokenDto login(String userId, String password) throws Exception;

    UserDto readMember(String token) throws Exception;
}
