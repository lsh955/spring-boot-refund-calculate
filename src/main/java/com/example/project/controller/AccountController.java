package com.example.project.controller;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.controller.dto.UserDto;
import com.example.project.enums.AccountStatus;
import com.example.project.service.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 이승환
 * @since 2022-02-18
 *
 * 계정에 관련된 Controller
 */
@RestController
@RequestMapping(value = "/szs", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * 회원가입 등록
     *
     * @param userDto   userDto
     * @return          message
     */
    @ApiOperation(value = "회원가입", notes = "입력에 따른 사용자 정보등록")
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccountStatus signup(@RequestBody UserDto userDto) throws Exception {

        return accountService.addSignup(
                userDto.getUserId(),
                userDto.getPassword(),
                userDto.getName(),
                userDto.getRegNo()
        );
    }

    /**
     * 로그인
     *
     * @param userDto   userDto
     * @return          User Token
     */
    @ApiOperation(value = "로그인", notes = "사용자검증에 따른 토큰발급")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JwtTokenDto login(@RequestBody UserDto userDto) throws Exception {

        return accountService.login(
                userDto.getUserId(),
                userDto.getPassword()
        );
    }

    /**
     * 개인정보 보기
     *
     * @param jwtTokenDto   User Token
     * @return              개인정보
     */
    @ApiOperation(value = "개인정보 보기", notes = "토큰정보에 따른 사용자 정보조회")
    @PostMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto member(@RequestBody JwtTokenDto jwtTokenDto) throws Exception {

        return accountService.readMember(
                jwtTokenDto.getToken()
        );
    }
}
