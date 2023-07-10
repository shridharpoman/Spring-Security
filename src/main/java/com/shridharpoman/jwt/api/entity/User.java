package com.shridharpoman.jwt.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.shridharpoman.jwt.api.util.Views;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.AUTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER_TBL")
public class User {
    @Id @GeneratedValue(strategy = AUTO) @JsonView(Views.Public.class)
    private int id;
    @JsonView(Views.Public.class)
    private  String name;
    private String password;
    @JsonView(Views.Public.class)
    private String email;
    @ManyToMany(fetch = EAGER)  @JsonView(Views.Public.class)
    private Collection<Role> roles = new ArrayList<>();
}
