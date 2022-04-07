package com.example.project.app.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이승환
 * @since 2022-02-20
 * <p>
 * 계층간 Jwt Token 교환을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtTokenDto {

    private String token;

    @Builder
    public JwtTokenDto(String token) {

        this.token = token;
    }
}
