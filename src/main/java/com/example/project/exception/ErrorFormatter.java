package com.example.project.exception;

import com.example.project.app.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

/**
 * @author 이승환
 * @since 2022-03-13
 */
@Getter
@Builder
public class ErrorFormatter {

    private final String code;
    private final String message;

    /**
     * 실제로 보낼 응답 Format
     *
     * @param errorCode errorCode
     * @return ResponseEntity
     */
    public static ResponseEntity<ErrorFormatter> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorFormatter.builder()
                        .code(errorCode.name())
                        .message(errorCode.getDetail())
                        .build()
                );
    }
}
