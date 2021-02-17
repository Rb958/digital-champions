package com.sabc.digitalchampions.repository;

import com.sabc.digitalchampions.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("select u from User u where u.username like %?1% ")
    Page<User> findAll(String username, Pageable pageable);

    boolean existsByRef(String ref);

    User findByRef(String ref);

    void deleteByRef(String ref);

    boolean existsByUsername(String username);
}
