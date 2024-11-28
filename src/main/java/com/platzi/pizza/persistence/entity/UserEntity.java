package com.platzi.pizza.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name="user")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name="user_name", nullable = false, length = 20)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(length = 50)
    private String email;

    @Column(nullable = false, columnDefinition="TINYINT")
    private Boolean locked;

    @Column(nullable = false, columnDefinition="TINYINT")
    private Boolean disable;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRoleEntity> roles;
}
