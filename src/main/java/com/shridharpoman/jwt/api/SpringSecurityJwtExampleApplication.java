package com.shridharpoman.jwt.api;

import com.shridharpoman.jwt.api.entity.Role;
import com.shridharpoman.jwt.api.entity.User;
import com.shridharpoman.jwt.api.repository.RoleRepository;
import com.shridharpoman.jwt.api.repository.UserRepository;
import com.shridharpoman.jwt.api.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringSecurityJwtExampleApplication {



    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtExampleApplication.class, args);
    }


    @Bean
    CommandLineRunner run(UserService userService){
        return args -> {
          userService.saveRole(new Role(1,"ROLE_ADMIN"));
          userService.saveRole(new Role(2,"ROLE_USER"));
          userService.saveRole(new Role(3,"ROLE_SUPER_ADMIN"));

          userService.saveUser(new User(1,"Shridhar","pass","spoman@gmail.com",new ArrayList<>()));
          userService.saveUser(new User(2,"poman","1234","spoman1@gmail.com",new ArrayList<>()));
          userService.saveUser(new User(3,"neha","3333","spoman2@gmail.com",new ArrayList<>()));
          userService.saveUser(new User(4,"patil","pa444ss","spoman3@gmail.com",new ArrayList<>()));

          userService.addRoleToUser("spoman@gmail.com","ROLE_ADMIN");
          userService.addRoleToUser("spoman@gmail.com","ROLE_SUPER_ADMIN");
          userService.addRoleToUser("spoman@gmail.com","ROLE_USER");
          userService.addRoleToUser("spoman1@gmail.com","ROLE_USER");
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
