package com.example.project.app.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 이승환
 * @since 2022-02-18
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByRegNo(String regNo);

    User save(User user);

    Optional<User> findByUserId(String userId);

    Optional<User> findByNameAndRegNo(String name, String regNo);

    @Query(
            value = "	SELECT user_idx " +
                    "	FROM user" +
                    "	WHERE name = ?1 and reg_no = ?2",
            nativeQuery = true
    )
    Long findByUserIdx(String name, String regNo);
}
