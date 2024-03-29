package com.example.Sakila.API.Repository;
import com.example.Sakila.API.Model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    @Query("SELECT film FROM Film film WHERE film.title LIKE CONCAT('%', UPPER(:titleInput), '%')")
    Iterable<Film> findByTitleContains(@Param("titleInput") String titleInput);

    @Query("SELECT film FROM Film film JOIN film.categorySet cat WHERE cat.categoryId = :id")
    Iterable<Film> findByCategoryId(@Param("id") int id);

    @Query("SELECT film FROM Film film JOIN film.categorySet cat WHERE cat.name = :catName")
    Iterable<Film> findByCategoryName(@Param("catName") String catName);

    @Query("SELECT film FROM Film film WHERE film.title = :titleInput")
    Optional<Film> findByTitleEquals(@Param("titleInput") String titleInput);

    @Query("SELECT film FROM Film film JOIN film.actors actor WHERE actor.actorId = :id")
    Iterable<Film> findByActorId(@Param("id") int id);

    @Query("SELECT film FROM Film film WHERE film.rating = :rat")
    Iterable<Film> findByRating(@Param("rat") String rat);

    @Query("SELECT film FROM Film film JOIN film.categorySet cat WHERE cat.categoryId = :catId AND film.rating = :rat")
    Iterable<Film> findByCatAndRat(@Param("catId") int catId, @Param("rat") String rat);
}
