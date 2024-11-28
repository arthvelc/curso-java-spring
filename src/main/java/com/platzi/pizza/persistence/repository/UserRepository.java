package com.platzi.pizza.persistence.repository;

import com.platzi.pizza.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {

    //vamos a escribir nuestra lógica
}
