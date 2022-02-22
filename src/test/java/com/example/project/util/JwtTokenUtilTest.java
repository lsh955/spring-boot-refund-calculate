package com.example.project.util;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.jar.JarException;

/**
 * @author 이승환
 * @since 2022-02-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void 토큰생성() {
        HashMap<String, String> token = this.jwtTokenUtil.createToken("홍길동", "860824-1655068");
        System.out.println(token);
    }

    @Test
    public void 토큰검증() throws JarException {
        HashMap<String, String> token = this.jwtTokenUtil.createToken("홍길동", "860824-1655068");
        System.out.println(token);

        System.out.println(jwtTokenUtil.validateToken(token.get("token")));
    }
}
