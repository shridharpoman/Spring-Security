package com.shridharpoman.jwt.api.service;

import com.shridharpoman.jwt.api.entity.Role;
import com.shridharpoman.jwt.api.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String username);
    List<User> getUsers();
}
