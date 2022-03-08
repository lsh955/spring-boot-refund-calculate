package com.example.project.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author 이승환
 * @since 2022-02-20
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JwtTokenStatus {

    TOKEN_SUCCESS("TOKEN_ERR_0001", "토큰 검증에 성공하였습니다."),
    TOKEN_FAILURE("TOKEN_ERR_0002", "토큰 검증에 실패하였습니다.");

    private final String code;
    private final String message;

    JwtTokenStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
