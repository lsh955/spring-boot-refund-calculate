package com.example.project.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이승환
 * @since 2022-02-20
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtTokenDto {

    private String token;

    @Deprecated
    @Builder
    public JwtTokenDto(String token) {

        this.token = token;
    }
}
