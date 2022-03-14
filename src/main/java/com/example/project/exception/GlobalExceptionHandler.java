package com.example.project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author 이승환
 * @since 2022-03-13
 * <p>
 * Exception 종류별로 예외를 잡아서 처리,
 * Exception 발생 시 넘겨받은 ErrorCode 를 사용해서 에러 메세지 정의
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 직접 정의한 CustomException 을 사용
    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ErrorFormatter> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorFormatter.toResponseEntity(e.getErrorCode());
    }
}
