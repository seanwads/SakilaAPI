package com.example.Sakila.API.Repository;

import com.example.Sakila.API.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
