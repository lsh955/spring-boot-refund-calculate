package com.example.project.app.account.dto;

import com.example.project.app.account.domain.User;
import com.example.project.app.common.util.AESCryptoUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이승환
 * @since 2022-02-18
 * <p>
 * 계층간 사용자정보 교환을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private String userId;
    private String password;
    private String name;
    private String regNo;

    @Deprecated // 테스트 코드에서 사용하는 용도라 프로덕션 환경에서는 Deprecated
    @Builder
    public UserDto(String userId,
                   String password,
                   String name,
                   String regNo) {

        this.userId = userId;
        this.password = password;
        this.name = name;
        this.regNo = regNo;
    }

    // DTO -> Entity
    public User toEntity() {
        AESCryptoUtil aesCryptoUtil = new AESCryptoUtil();

        return User.builder()
                .userId(userId)
                .password(aesCryptoUtil.encrypt(password))
                .name(name)
                .regNo(aesCryptoUtil.encrypt(regNo))
                .build();
    }
}
