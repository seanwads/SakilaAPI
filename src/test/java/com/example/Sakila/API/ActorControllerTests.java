package com.example.Sakila.API;

import com.example.Sakila.API.Controller.FilmController;
import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Repository.FilmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmController.class)
class ActorControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    FilmRepository filmRepository;

    int testId = 1;
    String testTitle = "ABSOLUTE DINOSAUR";

    @Test
    void getFilmTest() throws Exception {
        Film film = new Film(testId, testTitle);

        when(filmRepository.findById(testId)).thenReturn(Optional.of(film));

        mockMvc.perform(get("/film/get/" + testId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filmId").value(film.getFilmId()))
                .andExpect(jsonPath("$.title").value(film.getTitle()));
    }
}
