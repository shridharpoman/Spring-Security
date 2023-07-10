package com.shridharpoman.jwt.api.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shridharpoman.jwt.api.entity.Role;
import com.shridharpoman.jwt.api.entity.User;
import com.shridharpoman.jwt.api.repository.UserProjection;
import com.shridharpoman.jwt.api.service.UserService;
import com.shridharpoman.jwt.api.util.Views;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserProjection>>getLimitedUserFields(){
        return ResponseEntity.ok().body(userService.getLimitedUserFields());
    }

    @PostMapping("/user/save") @JsonView(Views.Public.class)
    public ResponseEntity<User>saveUser(@RequestBody User user){
        //indication that a new user resource has been created and provide the URI for accessing that resource in the response headers
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        //indication that a new role resource has been created and provide the URI for accessing that resource in the response headers
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getEmail(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String authorization = request.getHeader(AUTHORIZATION);
      if(authorization !=null && authorization.startsWith("Bearer ")){
            try {
                // Extracts the refresh token from the authorization string.
                String refresh_token = authorization.substring("Bearer ".length());

                // Creates an HMAC256 algorithm with the "secret" key.
                Algorithm algo  = Algorithm.HMAC256("secret".getBytes());

                // Creates a verifier with the specified algorithm.
                JWTVerifier verifier = JWT.require(algo).build();

                // Verifies the refresh token and obtains the decoded JWT.
                DecodedJWT decodedJWT = verifier.verify(refresh_token);

                // Retrieves the username (subject) from the decoded JWT.
                String username = decodedJWT.getSubject();

                User user = userService.getUser(username);

                String new_access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algo);

                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",new_access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(),tokens);


            }catch(Exception exception){
                response.setHeader("Error",exception.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);

          }
      }else {
          throw new RuntimeException("Refresh token is missing");
      }
    }
}

@Data
class RoleToUserForm {
    private String email;
    private String roleName;
}
