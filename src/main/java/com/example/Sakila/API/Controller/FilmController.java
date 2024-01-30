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
    @Autowired
    private FilmRepository filmRepository;

    @GetMapping("/get/{id}")
    private Film getFilmById(@PathVariable("id") int filmId){
        return filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException("Film not found"));
    }

    @GetMapping("/getAll")
    private Iterable<Film> getAllFilms(){
        return filmRepository.findAll();
    }

    @GetMapping("/getCategories/{id}")
    private Set<Category> getCategoriesByFilm(@PathVariable("id") int filmId){
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException("Film not found"));
        return film.getCategorySet();
    }

    @GetMapping("/getByTitle/{title}")
    private Iterable<Film> getFilmsByTitle(@PathVariable("title") String titleInput){
        return filmRepository.findByTitleContains(titleInput);
    }

    @GetMapping("/getByCatId/{id}")
    private Iterable<Film> getFilmsByCatId(@PathVariable("id") int catId){
        return filmRepository.findByCategoryId(catId);
    }

    @GetMapping("/getByCatName/{catName}")
    private Iterable<Film> getFilmsByCatName(@PathVariable("catName") String catName){
        return filmRepository.findByCategoryName(catName);
    }

    @PutMapping("/update/{id}")
    private Film updateFilmById(
            @PathVariable("id") int filmId,
            @RequestBody Film updatedFilm
    ){
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException("Film not found"));

        film.setTitle(updatedFilm.getTitle());
        film.setDescription(updatedFilm.getDescription());
        film.setRating(updatedFilm.getRating());
        film.setLanguageId(updatedFilm.getLanguageId());
        film.setCategorySet(updatedFilm.getCategorySet());

        return filmRepository.save(film);
    }

    @PostMapping("/create")
    private Film createFilm(@RequestBody Film newFilm){
        return filmRepository.save(newFilm);
    }

    @DeleteMapping("/delete/{id}")
    private void deleteFilmById(@PathVariable("id") int filmId){
        filmRepository.deleteById(filmId);
    }
}
