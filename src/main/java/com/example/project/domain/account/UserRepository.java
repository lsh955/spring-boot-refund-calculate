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

    User findByNameAndRegNo(String name, String regNo);
}
