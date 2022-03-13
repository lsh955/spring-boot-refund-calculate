package com.example.project.domain.scrap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author 이승환
 * @since 2022-02-21
 */
@Repository
public interface ScrapOneRepository extends JpaRepository<ScrapOne, Long> {

    @Query(
            value = "	SELECT total_pay " +
                    "	FROM scrap_one" +
                    "	WHERE user_idx = ?1",
            nativeQuery = true
    )
    Long findByTotalPay(Long userId);
}
