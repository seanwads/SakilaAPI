package com.example.Sakila.API.Repository;
import com.example.Sakila.API.Model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Integer> {

}
