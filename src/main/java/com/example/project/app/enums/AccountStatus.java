package com.example.project.app.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author 이승환
 * @since 2022-02-19
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AccountStatus {

    SIGNUP_SUCCESS("ACCOUNT_ERR_0001", "가입성공"),
    SIGNUP_FAILURE("ACCOUNT_ERR_0002", "가입실패"),
    NAME_OVERLAP("ACCOUNT_ERR_0003", "사용자이름 중복"),
    REG_NO_OVERLAP("ACCOUNT_ERR_0004", "주민번호 중복"),
    UNABLE_TO_NAME("ACCOUNT_ERR_0005", "가입 가능한 사용자이름이 아닙니다"),
    UNABLE_TO_REG_NO("ACCOUNT_ERR_0006", "가입 가능한 주민번호가 아닙니다"),
    UNKNOWN("ACCOUNT_ERR_0007", "정보가 올바르지 않거나 가입되지 않는 정보입니다.");

    private final String code;
    private final String message;

    AccountStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
