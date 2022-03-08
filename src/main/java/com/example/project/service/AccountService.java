package com.example.project.service;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.controller.dto.UserDto;
import com.example.project.domain.account.JoinAvailableRepository;
import com.example.project.domain.account.User;
import com.example.project.domain.account.UserRepository;
import com.example.project.enums.AccountStatus;
import com.example.project.util.AESCryptoUtil;
import com.example.project.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * @author 이승환
 * @since 2022-02-18
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final JoinAvailableRepository joinAvailableRepository;

    private final JwtTokenUtil jwtTokenUtil;
    private final AESCryptoUtil aesCryptoUtil;

    /**
     * 회원가입
     *
     * @param userDto userDto
     * @return
     */
    @Transactional
    public AccountStatus addSignup(UserDto userDto) throws Exception {
        // 패스워드 암호화
        String encryptedRegNo = this.aesCryptoUtil.encrypt(userDto.getRegNo());

        // 가입가능한 주민번호 체크(이름은 동명이인 있을 수 있으니 가입가능한 이름은 검사하지 않음)
        if (!this.joinAvailableRepository.existsByRegNo(encryptedRegNo))
            return AccountStatus.UNABLE_TO_REG_NO;

        // 주민번호 중복체크
        if (this.userRepository.existsByRegNo(encryptedRegNo))
            return AccountStatus.REG_NO_OVERLAP;

        // 사용자 등록
        this.userRepository.save(userDto.toEntity());

        return AccountStatus.SIGNUP_SUCCESS;
    }

    /**
     * 로그인
     *
     * @param userDto userDto
     * @return
     */
    @Transactional
    public Object login(UserDto userDto) throws Exception {
        // 사용자 아이디 기준으로 데이터 불러오기
        User user = userRepository.findByUserId(userDto.getUserId());

        if (user == null)   // 정보가 없다면 가입되지 않는회원으로 간주
            return AccountStatus.INCONSISTENT;

        // 패스워드 복호화
        String decryptedDbPassword = this.aesCryptoUtil.decrypt(user.getPassword());

        // 패스와드가 맞는지 검증
        if (!userDto.getPassword().equals(decryptedDbPassword))
            return AccountStatus.INCONSISTENT;  // 정보가 올바르지 않는다면.

        return this.jwtTokenUtil.createToken(user.getName(), user.getRegNo());  // 가입이 되었다면 토큰생성.
    }

    /**
     * 내 정보 보기
     *
     * @param jwtTokenDto User Token
     * @return
     */
    @Transactional
    public Object readMember(JwtTokenDto jwtTokenDto) throws Exception {
        // Token 검증
        HashMap<String, String> strToken = this.jwtTokenUtil.decoderToken(jwtTokenDto);

        // 사용자정보 불러오기
        User user = this.userRepository.findByNameAndRegNo(
                strToken.get("name"),
                this.aesCryptoUtil.encrypt(strToken.get("regNo"))
        );

        if (user == null)   // 정보가 없다면 가입되지 않는회원으로 간주
            return AccountStatus.INCONSISTENT;

        return user;
    }
}
