package com.debttrack.platfrom.model;

import com.debttrack.platfrom.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;

    private String name;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonIgnore
    private boolean enabled = false;
    @JsonIgnore
    private boolean notificationsEnabled = true;
}

