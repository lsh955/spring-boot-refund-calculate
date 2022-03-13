package com.example.project.exception;

import com.example.project.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 이승환
 * @since 2022-03-13
 *
 * 전역으로 사용할 할 Exception
 */
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
}
