package com.example.Sakila.API.Repository;
import com.example.Sakila.API.Model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    @Query("SELECT actor FROM Actor actor INNER JOIN actor.filmsActedIn films WHERE films.filmId = :filmId")
    Iterable<Actor> findByFilmId(@Param("filmId") int filmId);

    @Query("SELECT actor FROM Actor actor INNER JOIN actor.filmsActedIn films WHERE films.title LIKE CONCAT('%', :filmTitle, '%')")
    Iterable<Actor> findByFilmTitle(@Param("filmTitle") String filmTitle);

    @Query("SELECT actor FROM Actor actor WHERE CONCAT(actor.firstName, ' ', actor.lastName) LIKE CONCAT('%', UPPER(:nameInput), '%') ")
    Iterable<Actor> findByNameContains(@Param("nameInput") String nameInput);
}
