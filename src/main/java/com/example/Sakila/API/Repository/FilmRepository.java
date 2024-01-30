package com.example.Sakila.API.Repository;
import com.example.Sakila.API.Model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Integer> {

}