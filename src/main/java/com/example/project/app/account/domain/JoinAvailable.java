package com.example.project.app.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author 이승환
 * @since 2022-02-19
 * <p>
 * 가입 가능한 유저정보
 */
@Entity
@Getter
@Table(name = "join_available")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinAvailable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long availableIdx;  // 식별값

    private String name;        // 이름
    private String regNo;       // 주민등록번호

    @Builder
    public JoinAvailable(String name, String regNo) {
        this.name = name;
        this.regNo = regNo;
    }
}
