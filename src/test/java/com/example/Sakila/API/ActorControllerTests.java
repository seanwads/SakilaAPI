package com.example.Sakila.API;

import com.example.Sakila.API.Controller.ActorController;
import com.example.Sakila.API.Model.Actor;
import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Repository.ActorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ActorController.class)
public class ActorControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ActorRepository actorRepository;

    static List<Actor> actors = new ArrayList<>();
    static List<Film> films = new ArrayList<>();

    @BeforeAll
    static void setTestData(){
        actors.addAll(List.of(
                new Actor(1, "Anna", "Andrews"),
                new Actor(2, "Ben", "Brown"),
                new Actor(3, "Cal", "Carlos")
        ));
        films.addAll(List.of(
                new Film(1, "Movie 1", "desc", Year.now(), 1, "PG", null, null, Set.of(actors.get(0), actors.get(1))),
                new Film(2, "Movie 2", "desc", Year.now(), 1, "PG", null, null, Set.of(actors.get(1), actors.get(2)))
        ));

        actors.get(0).setFilmsActedIn(Set.of(films.get(0)));
        actors.get(1).setFilmsActedIn(Set.of(films.get(0),films.get(1)));
        actors.get(2).setFilmsActedIn(Set.of(films.get(1)));
    }

    @Test
    void getActorByIdTest() throws Exception{
        int id = actors.get(0).getActorId();

        when(actorRepository.findById(id)).thenReturn(Optional.ofNullable(actors.get(0)));

        mockMvc.perform(get("/actor/get/" + 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actorId").value(actors.get(0).getActorId()))
                .andExpect(jsonPath("$.firstName").value(actors.get(0).getFirstName()))
                .andExpect(jsonPath("$.lastName").value(actors.get(0).getLastName()));
    }

    @Test
    void getAllActorsTest() throws Exception {
        when(actorRepository.findAll()).thenReturn(actors);

        mockMvc.perform(get("/actor/getAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].actorId").value(actors.get(0).getActorId()))
                .andExpect(jsonPath("$[0].firstName").value(actors.get(0).getFirstName()))
                .andExpect(jsonPath("$[1].actorId").value(actors.get(1).getActorId()))
                .andExpect(jsonPath("$[2].actorId").value(actors.get(2).getActorId()));
    }

    @Test
    void getByFilmIdTest() throws Exception {
        int id = films.get(0).getFilmId();

        List<Actor> actorList = new ArrayList<>();
        for(Actor actor : actors){
            if(actor.getFilmsActedIn().contains(films.get(0))){
                actorList.add(actor);
            }
        }

        when(actorRepository.findByFilmId(id)).thenReturn(actorList);

        mockMvc.perform(get("/actor/getByFilmId/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(actorList.size())))
                .andExpect(jsonPath("$[0].actorId").value(actorList.get(0).getActorId()))
                .andExpect(jsonPath("$[0].firstName").value(actorList.get(0).getFirstName()));

    }

    @Test
    void getActorsByNameContainsTest() throws Exception {
        String nameQuery = "Cal";

        List<Actor> actorList = new ArrayList<>();

        for(Actor actor : actors){
            if((actor.getFirstName() + " " + actor.getLastName()).contains(nameQuery)){
                actorList.add(actor);
            }
        }

        when(actorRepository.findByNameContains(nameQuery)).thenReturn(actorList);

        mockMvc.perform(get("/actor/getByNameContains/" + nameQuery).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(actorList.size())))
                .andExpect(jsonPath("$[0].actorId").value(actorList.get(0).getActorId()))
                .andExpect(jsonPath("$[0].firstName").value(actorList.get(0).getFirstName()));
    }

    @Test
    void updateActorTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        int id = 1;
        Actor updatedActor = actors.get(0);
        updatedActor.setFirstName("Amy");
        String actorBody = mapper.writeValueAsString(updatedActor);

        when(actorRepository.findById(id)).thenReturn(Optional.of(actors.get(0)));
        when(actorRepository.save(updatedActor)).thenReturn(updatedActor);

        mockMvc.perform(put("/actor/update/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actorBody))
                .andExpect(jsonPath("$.actorId").value(updatedActor.getActorId()))
                .andExpect(jsonPath("$.firstName").value(updatedActor.getFirstName()));
    }

    @Test
    void createActorTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Actor actorToCreate = new Actor(4, "Damian", "De Souza");
        String actorBody = mapper.writeValueAsString(actorToCreate);

        when(actorRepository.save(actorToCreate)).thenReturn(actorToCreate);

        mockMvc.perform(post("/actor/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actorBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteActorTest() throws Exception {
        int id = 1;
        mockMvc.perform(delete("/actor/delete/" + 1)).andExpect(status().isOk());
    }
}

