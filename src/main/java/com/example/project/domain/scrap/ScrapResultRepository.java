package com.example.project.domain.scrap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 이승환
 * @since 2022-02-21
 */
@Repository
public interface ScrapResultRepository extends JpaRepository<ScrapResult, Long> {
}
