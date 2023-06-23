package com.shridharpoman.jwt.api.repository;

import com.shridharpoman.jwt.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(String name);
}
