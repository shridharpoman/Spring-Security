package com.shridharpoman.jwt.api.repository;

import com.shridharpoman.jwt.api.entity.Role;

import java.util.List;

public interface UserProjection {
    Integer getId();
    String getName();
    String getEmail();
    List<Role> getRoles();
}
