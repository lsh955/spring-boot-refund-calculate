package com.example.project.app.account.service;

import com.example.project.app.account.dto.UserDto;
import com.example.project.app.common.dto.JwtTokenDto;

/**
 * @author 이승환
 * @since 2022-03-14
 */
public interface AccountService {

    UserDto addSignup(String userId, String password, String name, String regNo);

    JwtTokenDto login(String userId, String password);

    UserDto readMember(String token);
}
