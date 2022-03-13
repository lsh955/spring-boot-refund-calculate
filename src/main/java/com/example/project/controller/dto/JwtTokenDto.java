package com.example.project.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이승환
 * @since 2022-02-20
 *
 * 계층간 Jwt Token 교환을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtTokenDto {

    private String token;

    @Deprecated // 테스트 코드에서 사용하는 용도라 프로덕션 환경에서는 Deprecated
    @Builder
    public JwtTokenDto(String token) {

        this.token = token;
    }
}
