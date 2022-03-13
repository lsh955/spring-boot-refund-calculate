package com.example.project.domain.account;

import com.example.project.domain.scrap.ScrapList;
import com.example.project.domain.scrap.ScrapOne;
import com.example.project.domain.scrap.ScrapResponse;
import com.example.project.domain.scrap.ScrapTwo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 이승환
 * @since 2022-02-18
 * <p>
 * 가입 완료된 유저정보
 */
@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;       // 식별값

    @OneToMany(mappedBy="user")
    private List<ScrapOne> scrapOnes = new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<ScrapTwo> scrapTwos = new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<ScrapResponse> scrapResponses = new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<ScrapList> scrapLists = new ArrayList<>();

    private String userId;      // 아이디
    private String password;    // 패스워드
    private String name;        // 이름
    private String regNo;       // 주민등록번호

    @Builder
    public User(String userId,
                String password,
                String name,
                String regNo) {

        this.userId = userId;
        this.password = password;
        this.name = name;
        this.regNo = regNo;
    }
}
