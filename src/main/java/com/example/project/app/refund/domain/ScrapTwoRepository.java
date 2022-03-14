package com.example.project.app.refund.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author 이승환
 * @since 2022-02-21
 */
@Repository
public interface ScrapTwoRepository extends JpaRepository<ScrapTwo, Long> {

    @Query(
            value = "	SELECT total_used " +
                    "	FROM scrap_two" +
                    "	WHERE user_idx = ?1",
            nativeQuery = true
    )
    Long findByTotalUsed(Long userId);
}
