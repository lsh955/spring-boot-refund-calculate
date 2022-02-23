package com.example.project.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 이승환
 * @since 2022-02-18
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserId(String userId);

    boolean existsByName(String name);

    boolean existsByRegNo(String regNo);

    boolean existsByPassword(String password);

    User findByUserId(String userId);

    // TODO 최근데이터 반화가기
    User findByNameAndRegNo(String name, String regNo);
}
