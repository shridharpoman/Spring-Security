package com.shridharpoman.jwt.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROLE_TBL")
public class Role {
    @Id
    @GeneratedValue(strategy = AUTO)
    private int id;
    private String name;
}
