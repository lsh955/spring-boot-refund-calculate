package com.example.project.app.refund.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 이승환
 * @since 2022-02-21
 */
@Repository
public interface ScrapResponseRepository extends JpaRepository<ScrapResponse, Long> {
}
