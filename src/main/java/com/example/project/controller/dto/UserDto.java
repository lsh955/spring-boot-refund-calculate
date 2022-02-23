package com.example.project.controller.dto;

import com.example.project.domain.account.User;
import com.example.project.util.AESCryptoUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 이승환
 * @since 2022-02-18
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    @NotBlank(message = "userId must not be empty")
    private String userId;

    @NotBlank(message = "password must not be empty")
    private String password;

    @NotBlank(message = "name must not be empty")
    private String name;

    @NotBlank(message = "regNo must not be empty")
    private String regNo;

    @Builder
    public UserDto(String userId, String password, String name, String regNo) {

        this.userId = userId;
        this.password = password;
        this.name = name;
        this.regNo = regNo;
    }

    public User toEntity() throws Exception {
        AESCryptoUtil aesCryptoUtil = new AESCryptoUtil();

        return User.builder()
                .userId(userId)
                .password(aesCryptoUtil.encrypt(password))
                .name(name)
                .regNo(aesCryptoUtil.encrypt(regNo))
                .build();
    }
}
