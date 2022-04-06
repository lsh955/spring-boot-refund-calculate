package com.example.project.app.account.controller;

import com.example.project.app.account.dto.UserDto;
import com.example.project.app.account.service.AccountService;
import com.example.project.app.common.dto.JwtTokenDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 이승환
 * @since 2022-02-18
 * <p>
 * 계정에 관련된 Controller
 */
@Slf4j
@RestController
@RequestMapping(value = "/szs", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * 회원가입 등록
     *
     * @param userDto userDto
     * @return message
     */
    @ApiOperation(value = "회원가입", notes = "입력에 따른 사용자 정보등록")
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signup(@RequestBody UserDto userDto) {

        UserDto result = accountService.addSignup(
                userDto.getUserId(),
                userDto.getPassword(),
                userDto.getName(),
                userDto.getRegNo()
        );

        return ResponseEntity.ok(result);
    }

    /**
     * 로그인
     *
     * @param userDto userDto
     * @return User Token
     */
    @ApiOperation(value = "로그인", notes = "사용자검증에 따른 토큰발급")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokenDto> login(@RequestBody UserDto userDto) {

        JwtTokenDto result = accountService.login(
                userDto.getUserId(),
                userDto.getPassword()
        );

        return ResponseEntity.ok(result);
    }

    /**
     * 개인정보 보기
     *
     * @param token User Token
     * @return  개인정보
     */
    @ApiOperation(value = "개인정보 보기", notes = "토큰정보에 따른 사용자 정보조회")
    @GetMapping(value = "/me")
    public ResponseEntity<UserDto> member(@RequestHeader("Authorization") String token) {

        UserDto result = accountService.readMember(token);

        return ResponseEntity.ok(result);
    }
}