package com.example.Sakila.API;

import com.example.Sakila.API.Controller.FilmController;
import com.example.Sakila.API.Model.Actor;
import com.example.Sakila.API.Model.Category;
import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Repository.FilmRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Year;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FilmRepository filmRepository;

    static List<Film> testData = new ArrayList<>();
    static Category action;
    static Category horror;
    static Actor john;
    static Actor jane;

    @BeforeAll
    static void setTestData(){
        action = new Category(1,"Action");
        horror = new Category(1, "Horror");
        john = new Actor(1,"John", "Doe");
        jane = new Actor(2, "Jane", "Doe");

        testData.add(new Film(
                1,
                "Film 1",
                "test film",
                Year.now(),
                1,
                "PG-13",
                Set.of(action),
                null,
                Set.of(john)));

        testData.add(new Film(
                2,
                "Film 2",
                "test film",
                Year.now(),
                1,
                "PG-13",
                Set.of(action, horror),
                null,
                Set.of(john, jane)));

        testData.add(new Film(
                3,
                "Film 3",
                "test film",
                Year.now(),
                1,
                "PG-13",
                Set.of(horror),
                null,
                Set.of(jane)));
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
}
