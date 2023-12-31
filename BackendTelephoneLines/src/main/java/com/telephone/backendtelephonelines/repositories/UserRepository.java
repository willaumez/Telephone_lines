package com.telephone.backendtelephonelines.repositories;

import com.telephone.backendtelephonelines.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT c FROM User c WHERE c.username LIKE %:kw% OR c.email LIKE %:kw%")
    List<User> searchUser(@Param("kw") String keyword);

    User findByUsernameOrEmail(String username, String email);

   /* @Query("SELECT c FROM User c WHERE c.email = :kw OR c.username = :kw")
    User findUserByUserNameOrEmail(@Param("kw") String keyword);*/
}
