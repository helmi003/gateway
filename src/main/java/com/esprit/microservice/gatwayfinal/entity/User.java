package com.esprit.microservice.gatwayfinal.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    private String id;
    public String username;
    public String firstName;
    public String lastName;
    public String password;
    public String email;
    public String image;
    @Enumerated(EnumType.STRING)
    private Role role;
    public boolean approve = false;
    public boolean block = false;
}