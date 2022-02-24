package com.example.project.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author 이승환
 * @since 2022-02-23
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScrapStatus {

    NO_SCRAP_DATA("SCRAP_ERR_0001", "요청한 데이터가 존재하지 않습니다.");

    private final String code;
    private final String message;

    ScrapStatus(String code, String message) {
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
