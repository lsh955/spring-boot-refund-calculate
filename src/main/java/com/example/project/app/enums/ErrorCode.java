package com.example.project.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * @author 이승환
 * @since 2022-03-13
 *
 * Exception ErrorCode
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    NAME_OVERLAP(CONFLICT, "중복된 사용자 이름 입니다."),
    REG_NO_OVERLAP(CONFLICT, "중복된 주민등록번호 입니다."),
    UNABLE_TO_NAME(CONFLICT, "가입 가능한 사용자이름이 아닙니다"),
    UNABLE_TO_REG_NO(CONFLICT, "가입 가능한 주민번호가 아닙니다"),
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    // 401
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 정보가 존재하지 않습니다"),
    UNAUTHORIZED_PASSWORD(UNAUTHORIZED, "패스워드가 올바르지 않습니다."),

    // 404,
    NO_SCRAP_DATA(NOT_FOUND, "요청한 데이터가 존재하지 않습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "사용자 정보를 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String detail;
}
