package com.example.project.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 이승환
 * @since 2022-02-19
 */
@Repository
public interface JoinAvailableRepository extends JpaRepository<JoinAvailable, Long> {

    boolean existsByRegNo(String regNo);
}
