package com.example.Sakila.API.Controller;

import com.example.Sakila.API.Model.Category;
import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.Set;

@CrossOrigin
@RequestMapping("/film")
@RestController
public class FilmController {

    private final FilmRepository filmRepository;
    private static final String notFoundResponse = "Film not found";

    @Autowired
    public FilmController(FilmRepository filmRepository){
        this.filmRepository = filmRepository;
    }

    @GetMapping("/get/{id}")
    public Film getFilmById(@PathVariable("id") int filmId){
        return filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
    }

    @GetMapping("/getAll")
    public Iterable<Film> getAllFilms(){
        return filmRepository.findAll();
    }

    @GetMapping("/getCategories/{id}")
    public Set<Category> getCategoriesByFilm(@PathVariable("id") int filmId){
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
        return film.getCategorySet();
    }

    @GetMapping("/getByTitle/{title}")
    public Iterable<Film> getFilmsByTitle(@PathVariable("title") String titleInput){
        return filmRepository.findByTitleContains(titleInput);
    }

    @GetMapping("/getIdByTitle/{title}")
    public Integer getFilmIdByTitle(@PathVariable("title") String titleInput){
        Film film = filmRepository.findByTitleEquals(titleInput).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
        return film.getFilmId();
    }

    @GetMapping("/getByCatId/{id}")
    public Iterable<Film> getFilmsByCatId(@PathVariable("id") int catId){
        return filmRepository.findByCategoryId(catId);
    }

    @GetMapping("/getByCatName/{catName}")
    public Iterable<Film> getFilmsByCatName(@PathVariable("catName") String catName){
        return filmRepository.findByCategoryName(catName);
    }

    @PutMapping("/update/{id}")
    public Film updateFilmById(
            @PathVariable("id") int filmId,
            @RequestBody Film updatedFilm
    ){
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));

        film.setTitle(updatedFilm.getTitle());
        film.setDescription(updatedFilm.getDescription());
        film.setRating(updatedFilm.getRating());
        film.setLanguageId(updatedFilm.getLanguageId());
        film.setCategorySet(updatedFilm.getCategorySet());

        return filmRepository.save(film);
    }

    @PostMapping("/create")
    public Film createFilm(@RequestBody Film newFilm){
        return filmRepository.save(newFilm);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFilmById(@PathVariable("id") int filmId){
        filmRepository.deleteById(filmId);
    }
}
