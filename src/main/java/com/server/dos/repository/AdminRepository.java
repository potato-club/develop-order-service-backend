package com.server.dos.repository;

import com.server.dos.entity.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    boolean existsByAdminId(String adminId);

    Admin findByAdminId(String adminId);

    List<Admin> findAll();
}
