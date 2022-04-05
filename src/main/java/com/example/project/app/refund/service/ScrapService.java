package com.example.project.app.refund.service;

import com.example.project.app.refund.dto.ScrapDto;

/**
 * @author 이승환
 * @since 2022-03-14
 */
public interface ScrapService {

    ScrapDto getSaveByScrap(String token);
}
