package com.platzi.pizza.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@IdClass(UserRoleId.class)
@Getter
@Setter
@NoArgsConstructor
public class UserRoleEntity {
    @Id
    @Column(name="user_name",nullable = false, length = 20)
    private String userName;

    @Id
    @Column(nullable = false, length = 20)
    private String role;

    @Column(name="greated_date", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime greatedDate;

    //ahora vamos a escribir la relación que existe con UserEntity

    @ManyToOne
    @JoinColumn(name = "user_name", referencedColumnName = "user_name", updatable = false, insertable = false)
    private UserEntity user;
}
