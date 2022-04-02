package com.example.project.app.account.service;

import com.example.project.app.account.domain.JoinAvailableRepository;
import com.example.project.app.account.domain.User;
import com.example.project.app.account.domain.UserRepository;
import com.example.project.app.account.dto.UserDto;
import com.example.project.app.common.dto.JwtTokenDto;
import com.example.project.app.common.enums.AccountStatus;
import com.example.project.app.common.enums.ErrorCode;
import com.example.project.app.common.util.AESCryptoUtil;
import com.example.project.app.common.util.JwtTokenUtil;
import com.example.project.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

import static com.example.project.app.common.enums.ErrorCode.*;

/**
 * @author 이승환
 * @since 2022-02-18
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {

    private final UserRepository userRepository;
    private final JoinAvailableRepository joinAvailableRepository;

    private final JwtTokenUtil jwtTokenUtil;
    private final AESCryptoUtil aesCryptoUtil;

    /**
     * 회원가입
     *
     * @param userId    아이디
     * @param password  패스워드
     * @param name      이름
     * @param regNo     주민등록번호
     * @return          성공여부
     */
    @Override
    public AccountStatus addSignup(final String userId,
                                   final String password,
                                   final String name,
                                   final String regNo) throws Exception {
        // 패스워드 암호화
        final String encryptedRegNo = this.aesCryptoUtil.encrypt(regNo);
        final String encryptedPassword = this.aesCryptoUtil.encrypt(password);

        // 가입가능한 주민번호 체크(이름은 동명이인 있을 수 있으니 가입가능한 이름은 검사하지 않음)
        if (!this.joinAvailableRepository.existsByRegNo(encryptedRegNo))
            throw new CustomException(UNABLE_TO_REG_NO);

        // 주민번호 중복체크
        if (this.userRepository.existsByRegNo(encryptedRegNo))
            throw new CustomException(REG_NO_OVERLAP);

        // 사용자 등록
        this.userRepository.save(User.builder()
                .userId(userId)
                .password(encryptedPassword)
                .name(name)
                .regNo(encryptedRegNo)
                .build()
        );

        return AccountStatus.SIGNUP_SUCCESS; // 성공
    }

    /**
     * 로그인
     *
     * @param userId    아이디
     * @param password  패스워드
     * @return          성공여부
     */
    @Override
    public JwtTokenDto login(final String userId, final String password) throws Exception {
        // 사용자 아이디 기준으로 데이터 불러오기
        final User user = getFindByUserId(userId);

        // 패스워드 복호화
        final String decryptedDbPassword = this.aesCryptoUtil.decrypt(user.getPassword());

        // 패스와드가 맞는지 검증
        if (!password.equals(decryptedDbPassword))
            throw new CustomException(UNAUTHORIZED_PASSWORD);

        // 가입이 되었다면 토큰생성.
        final HashMap<String, String> token = this.jwtTokenUtil.createToken(
                user.getName(),
                user.getRegNo()
        );

        return JwtTokenDto.builder()
                .token(token.get("token"))
                .build();
    }

    /**
     * 내 정보 보기
     *
     * @param token User Token
     * @return      성공여부
     */
    @Override
    public UserDto readMember(final String token) throws Exception {
        // Token 검증
        final HashMap<String, String> strToken = this.jwtTokenUtil.decoderToken(token);

        // 주민등록번호 암호화
        final String encryptRegNo = this.aesCryptoUtil.encrypt(strToken.get("regNo"));

        // 사용자정보 불러오기
        final User user = getFindByNameAndRegNo(strToken.get("name"), encryptRegNo);

        return UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .password(user.getPassword())
                .regNo(user.getRegNo())
                .build();
    }

    // TODO :: 아래의 사용자정보 가져오기는 하나로 통일되게 리렉토링 할 것.

    /**
     * 사용자정보 가져오기
     *
     * @param userId    사용자아이디
     * @return          사용자정보
     */
    private User getFindByUserId(final String userId) {
        final Optional<User> result = this.userRepository.findByUserId(userId);

        return result.orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }

    /**
     * 사용자정보 가져오기
     *
     * @param name  사용자이름
     * @param regNo 사용자주민번호
     * @return      사용자정보
     */
    private User getFindByNameAndRegNo(final String name, final String regNo) {
        final Optional<User> result = this.userRepository.findByNameAndRegNo(name, regNo);

        return result.orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }
}
