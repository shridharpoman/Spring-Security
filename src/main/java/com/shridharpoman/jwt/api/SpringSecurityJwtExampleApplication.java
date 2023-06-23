package com.shridharpoman.jwt.api;

import com.shridharpoman.jwt.api.entity.Role;
import com.shridharpoman.jwt.api.entity.User;
import com.shridharpoman.jwt.api.repository.RoleRepository;
import com.shridharpoman.jwt.api.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringSecurityJwtExampleApplication {
    @Autowired
    private UserRepository userRepositor;
    @Autowired
    private RoleRepository roleRepository;
    @PostConstruct
    public void initUsers(){

        List<Role> roles =Stream.of(
                new Role(1,"ADMIN"),
                new Role(2,"USER")

        ).collect(Collectors.toList());
        roleRepository.saveAll(roles);

//        List<User> users  = Stream.of(
//                new User(101,"Shridhar","password","spoman1@gmail.com","ADMIN"),
//                new User(102,"gaurav","password1","spoman@gmail.com")
//
//        ).collect(Collectors.toList());
//        userRepositor.saveAll(users);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtExampleApplication.class, args);
    }

}
