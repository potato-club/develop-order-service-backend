package com.server.dos.repository;


import com.server.dos.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
