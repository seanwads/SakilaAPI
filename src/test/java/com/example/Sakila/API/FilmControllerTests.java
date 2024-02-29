package com.example.Sakila.API;

import com.example.Sakila.API.Controller.FilmController;
import com.example.Sakila.API.Model.Actor;
import com.example.Sakila.API.Model.Category;
import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Repository.ActorRepository;
import com.example.Sakila.API.Repository.CatRepository;
import com.example.Sakila.API.Repository.FilmRepository;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Year;
import java.util.*;

import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FilmRepository filmRepository;

    @MockBean
    ActorRepository actorRepository;

    @MockBean
    CatRepository catRepository;

    List<Film> testData = new ArrayList<>();
    Category action;
    Category horror;
    Actor john;
    Actor jane;

    @BeforeEach
    void setTestData(){
        action = new Category("Action");
        horror = new Category("Horror");
        john = new Actor(1,"John", "Doe");
        jane = new Actor(2, "Jane", "Doe");

        testData.add(new Film(
                1,
                "Film 1",
                "test film",
                Year.now(),
                1,
                "PG13",
                Set.of(action),
                null,
                Set.of(john)));

        testData.add(new Film(
                2,
                "Film 2",
                "test film",
                Year.now(),
                1,
                "G",
                Set.of(action, horror),
                null,
                Set.of(john, jane)));

        testData.add(new Film(
                3,
                "Movie 3",
                "test film",
                Year.now(),
                1,
                "NC17",
                Set.of(horror),
                null,
                Set.of(jane)));

        john.setFilmsActedIn(Set.of(testData.get(0), testData.get(1)));
        jane.setFilmsActedIn(Set.of(testData.get(1), testData.get(2)));
        action.setFilmSet(Set.of(testData.get(0), testData.get(1)));
        horror.setFilmSet(Set.of(testData.get(1), testData.get(2)));
    }


    @Test
    void getFilmTest() throws Exception {
        int testIndex = 0;
        when(filmRepository.findById(testIndex+1)).thenReturn(Optional.of(testData.get(testIndex)));

        mockMvc.perform(get("/film/get/" + 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filmId").value(testData.get(testIndex).getFilmId()))
                .andExpect(jsonPath("$.title").value(testData.get(testIndex).getTitle()))
                .andExpect(jsonPath("$.description").value(testData.get(testIndex).getDescription()))
                .andExpect(jsonPath("$.languageId").value(testData.get(testIndex).getLanguageId()))
                .andExpect(jsonPath("$.rating").value(testData.get(testIndex).getRating()))
                .andExpect(jsonPath("$..categoryId").value(action.getCategoryId()))
                .andExpect(jsonPath("$..name").value(action.getName()))
                .andExpect(jsonPath("$..firstName").value(john.getFirstName()))
                .andExpect(jsonPath("$..lastName").value(john.getLastName()));
    }

    @Test
    void getAllFilmsTest() throws Exception {
        when(filmRepository.findAll()).thenReturn(testData);

        mockMvc.perform(get("/film/getAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(testData.size())))
                .andExpect(jsonPath("$[0].title").value(testData.get(0).getTitle()))
                .andExpect(jsonPath("$[1].title").value(testData.get(1).getTitle()))
                .andExpect(jsonPath("$[2].title").value(testData.get(2).getTitle()));
    }

    @Test
    void getCategoriesByFilmTest() throws Exception {
        int testIndex = 0;
        when(filmRepository.findById(testIndex+1)).thenReturn(Optional.of(testData.get(testIndex)));

        mockMvc.perform(get("/film/getCategories/" + 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..categoryId").value(action.getCategoryId()))
                .andExpect(jsonPath("$..name").value(action.getName()));
    }

    @Test
    void getByTitleTest() throws Exception {
        String testTitle = "Film";

        List<Film> filmList = new ArrayList<>();
        for(Film film : testData){
            if(film.getTitle().contains(testTitle)){
                filmList.add(film);
            }
        }

        when(filmRepository.findByTitleContains(testTitle)).thenReturn(filmList);

        mockMvc.perform(get("/film/getByTitle/" + testTitle).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(filmList.size())))
                .andExpect(jsonPath("$[0].title").value(testData.get(0).getTitle()))
                .andExpect(jsonPath("$[1].title").value(testData.get(1).getTitle()));
    }

    @Test
    void getByCatIdTest() throws Exception {
        int catId = action.getCategoryId();

        List<Film> filmList = new ArrayList<>();
        for(Film film : testData){
            if(film.getCategorySet().contains(action)){
                filmList.add(film);
            }
        }

        when(filmRepository.findByCategoryId(catId)).thenReturn(filmList);

        mockMvc.perform(get("/film/getByCatId/" + catId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(filmList.size())))
                .andExpect(jsonPath("$[0].title").value(testData.get(0).getTitle()))
                .andExpect(jsonPath("$[1].title").value(testData.get(1).getTitle()));
    }

    @Test
    void getByActorIdTest() throws Exception {
        int actorId = john.getActorId();

        List<Film> filmList = new ArrayList<>();
        for(Film film : testData){
            if(film.getActors().contains(john)){
                filmList.add(film);
            }
        }

        when(filmRepository.findByActorId(actorId)).thenReturn(filmList);

        mockMvc.perform(get("/film/getByActorId/" + actorId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(filmList.size())))
                .andExpect(jsonPath("$[0].title").value(testData.get(0).getTitle()))
                .andExpect(jsonPath("$[1].title").value(testData.get(1).getTitle()));
    }

    @Test
    void getByRatingTest() throws Exception {
        String rating = testData.get(0).getRating();

        List<Film> filmList = new ArrayList<>();
        for(Film film : testData){
            if(film.getRating().equals(rating)){
                filmList.add(film);
            }
        }

        when(filmRepository.findByRating(rating)).thenReturn(filmList);

        mockMvc.perform(get("/film/getByRating/" + rating).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(filmList.size())))
                .andExpect(jsonPath("$[0].title").value(testData.get(0).getTitle()));
    }

    @Test
    void getByCatAndRatTest() throws Exception {
        String rating = testData.get(1).getRating();
        int catId = action.getCategoryId();

        List<Film> filmList = new ArrayList<>();
        for(Film film : testData){
            if(film.getCategorySet().contains(action) && film.getRating().equals(rating)){
                filmList.add(film);
            }
        }

        when(filmRepository.findByCatAndRat(catId, rating)).thenReturn(filmList);

        mockMvc.perform(get("/film/getByCatAndRat/" + catId + "/" + rating).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(filmList.size())))
                .andExpect(jsonPath("$[0].title").value(testData.get(1).getTitle()));
    }

    @Test
    void updateFilmByIdTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        int testIndex = 0;
        String updatedDesc = "updated description";
        Film updatedFilm = testData.get(testIndex);
        updatedFilm.setDescription(updatedDesc);
        String updatedFilmBody = mapper.writeValueAsString(updatedFilm);

        when(filmRepository.findById(testIndex+1)).thenReturn(Optional.of(testData.get(testIndex)));
        when(filmRepository.save(updatedFilm)).thenReturn(updatedFilm);

        mockMvc.perform(put("/film/update/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedFilmBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.filmId").value(testData.get(testIndex).getFilmId()))
                .andExpect(jsonPath("$.description").value(updatedDesc));
    }

    @Test()
    void addActorByIdTest() throws Exception {
        int filmId = testData.get(2).getFilmId();
        int actorId = john.getActorId();

        when(filmRepository.findById(filmId)).thenReturn(Optional.ofNullable(testData.get(2)));
        when(actorRepository.findById(actorId)).thenReturn(Optional.of(john));
        when(actorRepository.save(john)).thenReturn(john);
        when(filmRepository.save(testData.get(2))).thenReturn(testData.get(2));

        mockMvc.perform(put("/film/addActor/" + filmId +"/" + actorId).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.filmId").value(filmId))
                .andExpect(jsonPath("$.actors", hasSize(2)));
    }

    @Test
    void removeActorByIdTest() throws Exception {
        int filmId = testData.get(0).getFilmId();
        int actorId = john.getActorId();

        when(filmRepository.findById(filmId)).thenReturn(Optional.ofNullable(testData.get(0)));
        when(actorRepository.findById(actorId)).thenReturn(Optional.of(john));
        when(actorRepository.save(john)).thenReturn(john);
        when(filmRepository.save(testData.get(0))).thenReturn(testData.get(0));

        mockMvc.perform(put("/film/removeActor/" + filmId +"/" + actorId).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.filmId").value(filmId))
                .andExpect(jsonPath("$.actors", hasSize(0)));
    }

    @Test
    void addCatById() throws Exception{
        int filmId = testData.get(0).getFilmId();
        int catId = horror.getCategoryId();

        when(filmRepository.findById(filmId)).thenReturn(Optional.ofNullable(testData.get(0)));
        when(catRepository.findById(catId)).thenReturn(Optional.of(horror));
        when(catRepository.save(horror)).thenReturn(horror);
        when(filmRepository.save(testData.get(0))).thenReturn(testData.get(0));

        mockMvc.perform(put("/film/addCat/" + filmId +"/" + catId).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.filmId").value(filmId))
                .andExpect(jsonPath("$.categorySet[-1:].categoryId").value(catId));
    }

    @Test
    void removeCatById() throws Exception{
        int filmId = testData.get(2).getFilmId();
        int catId = horror.getCategoryId();

        when(filmRepository.findById(filmId)).thenReturn(Optional.ofNullable(testData.get(2)));
        when(catRepository.findById(catId)).thenReturn(Optional.of(horror));
        when(catRepository.save(horror)).thenReturn(horror);
        when(filmRepository.save(testData.get(2))).thenReturn(testData.get(2));

        mockMvc.perform(put("/film/removeCat/" + filmId +"/" + catId).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.filmId").value(filmId))
                .andExpect(jsonPath("$.categorySet", hasSize(0)));
    }

    @Test
    void createFilmTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Film filmToCreate = new Film(
                4,
                "Movie 4",
                "test film",
                Year.now(),
                1,
                "G",
                Set.of(action),
                null,
                Set.of(jane));

        String filmBody = mapper.writeValueAsString(filmToCreate);

        when(filmRepository.save(filmToCreate)).thenReturn(filmToCreate);

        mockMvc.perform(post("/film/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmBody)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    void deleteFilmTest() throws Exception {
        int id = 1;
        mockMvc.perform(delete("/film/delete/" + id)).andExpect(status().isOk());
    }
}
