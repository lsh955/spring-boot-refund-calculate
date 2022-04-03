package com.example.project.app.account.service;

import com.example.project.app.account.dto.UserDto;
import com.example.project.app.common.dto.JwtTokenDto;

/**
 * @author 이승환
 * @since 2022-03-14
 */
public interface AccountService {

    UserDto addSignup(final String userId, final String password, final String name, final String regNo);

    JwtTokenDto login(final String userId, final String password);

    UserDto readMember(final String token);
}
