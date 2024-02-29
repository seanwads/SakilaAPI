package com.example.Sakila.API.Controller;

import com.example.Sakila.API.Model.Actor;
import com.example.Sakila.API.Model.Category;
import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Repository.ActorRepository;
import com.example.Sakila.API.Repository.CatRepository;
import com.example.Sakila.API.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin
@RequestMapping("/film")
@RestController
public class FilmController {

    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;
    private final CatRepository catRepository;
    private static final String notFoundResponse = "Film not found";

    @Autowired
    public FilmController(FilmRepository filmRepository, ActorRepository actorRepository, CatRepository catRepository){
        this.filmRepository = filmRepository;
        this.actorRepository = actorRepository;
        this.catRepository = catRepository;
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

    @GetMapping("/getByCatId/{id}")
    public Iterable<Film> getFilmsByCatId(@PathVariable("id") int catId){
        return filmRepository.findByCategoryId(catId);
    }

    @GetMapping("/getByActorId/{id}")
    public Iterable<Film> getFilmsByActorId(@PathVariable("id") int actorId){
        return filmRepository.findByActorId(actorId);
    }

    @GetMapping("/getByRating/{rating}")
    public Iterable<Film> getFilmsByRating(@PathVariable("rating") String rating){
        return filmRepository.findByRating(rating);
    }

    @GetMapping("/getByCatAndRat/{catId}/{rating}")
    public Iterable<Film> getFilmsByCatAndRat(@PathVariable("catId") int catId,
                                              @PathVariable("rating") String rating){
        return filmRepository.findByCatAndRat(catId, rating);
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
        film.setActors(updatedFilm.getActors());

        return filmRepository.save(film);
    }

    @PutMapping("/addActor/{filmId}/{actorId}")
    public Film addActorById(@PathVariable("filmId") int filmId,
                             @PathVariable("actorId") int actorId){
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
        Actor actorToAdd = actorRepository.findById(actorId).orElseThrow(() -> new ResourceAccessException("Actor not found"));

        Set<Actor> actorSet = new HashSet<>(film.getActors());

        if(!actorSet.contains(actorToAdd)){
            actorSet.add(actorToAdd);
            film.setActors(actorSet);

            Set<Film> filmSet = new HashSet<>(actorToAdd.getFilmsActedIn());
            filmSet.add(film);
            actorToAdd.setFilmsActedIn(filmSet);
            actorRepository.save(actorToAdd);
        }

        return filmRepository.save(film);
    }

    @PutMapping("/removeActor/{filmId}/{actorId}")
    public Film removeActorById(@PathVariable("filmId") int filmId,
                                @PathVariable("actorId") int actorId){
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
        Actor actorToRemove = actorRepository.findById(actorId).orElseThrow(() -> new ResourceAccessException("Actor not found"));

        Set<Actor> actorSet = new HashSet<>(film.getActors());
        actorSet.remove(actorToRemove);
        film.setActors(actorSet);

        Set<Film> filmSet = new HashSet<>(actorToRemove.getFilmsActedIn());
        filmSet.remove(film);
        actorToRemove.setFilmsActedIn(filmSet);
        actorRepository.save(actorToRemove);

        return filmRepository.save(film);
    }

    @PutMapping("/addCat/{filmId}/{catId}")
    public Film addCatById(@PathVariable("filmId") int filmId,
                           @PathVariable("catId") int catId){
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
        Category catToAdd = catRepository.findById(catId).orElseThrow(() -> new ResourceAccessException("Category not found"));

        Set<Category> catSet = new HashSet<>(film.getCategorySet());

        if(!catSet.contains(catToAdd)){
            catSet.add(catToAdd);
            film.setCategorySet(catSet);

            Set<Film> filmSet = new HashSet<>(catToAdd.getFilmSet());
            filmSet.add(film);
            catToAdd.setFilmSet(filmSet);
            catRepository.save(catToAdd);
        }

        return filmRepository.save(film);
    }

    @PutMapping("/removeCat/{filmId}/{catId}")
    public Film removeCatById(@PathVariable("filmId") int filmId,
                              @PathVariable("catId") int catId){
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
        Category catToRemove = catRepository.findById(catId).orElseThrow(() -> new ResourceAccessException("Category not found"));

        Set<Category> catSet = new HashSet<>(film.getCategorySet());
        catSet.remove(catToRemove);
        film.setCategorySet(catSet);

        Set<Film> filmSet = new HashSet<>(catToRemove.getFilmSet());
        filmSet.remove(film);
        catToRemove.setFilmSet(filmSet);
        catRepository.save(catToRemove);

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
