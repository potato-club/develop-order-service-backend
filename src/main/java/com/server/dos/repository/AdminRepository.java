package com.server.dos.repository;

import com.server.dos.entity.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    boolean existsByAdminId(String adminId);
    boolean existsByAdminEmail(String adminEmail);

    Admin findByAdminId(String adminId);
}
