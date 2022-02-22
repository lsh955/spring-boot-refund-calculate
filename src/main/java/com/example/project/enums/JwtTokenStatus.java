package com.example.project.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author 이승환
 * @since 2022-02-20
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JwtTokenStatus {

    TOKEN_FAILURE("TOKEN_ERR_0001", "토큰 검증에 실패하였습니다.");

    private final String code;
    private final String message;

    JwtTokenStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}