package com.example.project.controller;

import com.example.project.controller.dto.JwtTokenDto;
import com.example.project.controller.dto.ScrapDto;
import com.example.project.service.ScrapService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 이승환
 * @since 2022-02-20
 *
 * 스크랩에 관련된 Controller
 */
@RestController
@RequestMapping(value = "/szs", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    /**
     * 사용자 스크크정보 조회
     *
     * @param jwtTokenDto   User Token
     * @return              조회결과
     */
    @ApiOperation(value = "사용자 정보 스크랩", notes = "사용자정보를 기반으로 스크랩")
    @PostMapping(value = "/scrap", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ScrapDto scrap(@RequestBody JwtTokenDto jwtTokenDto) throws Exception {

        return scrapService.getSaveByScrap(
                jwtTokenDto.getToken()
        );
    }
}
