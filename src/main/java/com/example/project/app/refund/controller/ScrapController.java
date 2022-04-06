package com.example.project.app.refund.controller;

import com.example.project.app.refund.dto.ScrapDto;
import com.example.project.app.refund.service.ScrapService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 이승환
 * @since 2022-02-20
 * <p>
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
     * @param token User Token
     * @return  조회결과
     */
    @ApiOperation(value = "사용자 정보 스크랩", notes = "사용자정보를 기반으로 스크랩")
    @GetMapping(value = "/scrap")
    public ResponseEntity<ScrapDto> scrap(@RequestHeader("Authorization") String token) {

        ScrapDto result = scrapService.getSaveByScrap(token.substring(7));

        return ResponseEntity.ok(result);
    }
}
