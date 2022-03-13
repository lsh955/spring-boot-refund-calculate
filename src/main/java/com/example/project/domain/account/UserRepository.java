package com.example.project.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author 이승환
 * @since 2022-02-18
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByRegNo(String regNo);

    User save(User user);

    User findByUserId(String userId);

    User findByNameAndRegNo(String name, String regNo);

    @Query(
            value = "	SELECT user_idx " +
                    "	FROM user" +
                    "	WHERE name = ?1 and reg_no = ?2",
            nativeQuery = true
    )
    Long findByUserIdx(String name, String regNo);
}
