package com.example.Sakila.API;

import com.example.Sakila.API.Controller.UserController;
import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Model.Inventory;
import com.example.Sakila.API.Model.Rental;
import com.example.Sakila.API.Model.User;
import com.example.Sakila.API.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    static List<User> users = new ArrayList<>();

    static Film film;

    @BeforeAll
    static void setTestData() {
        users.addAll(List.of(
                new User("Andrew", "Abbot"),
                new User("Beth", "Bond"),
                new User("Chris", "Campbell")
        ));
        film = new Film(1, "Movie 1", "desc", Year.now(), 1, "PG", null, null, null);


        Inventory inventory = new Inventory();
        Rental rental1 = new Rental();
        Rental rental2 = new Rental();

        inventory.setInventoryFilm(film);
        rental1.setRentalInventory(inventory);
        rental2.setRentalInventory(inventory);

        rental1.setRentalCustomer(users.get(0));
        rental2.setRentalCustomer(users.get(1));

        users.get(0).setRentalSet(Set.of(rental1));
        users.get(1).setRentalSet(Set.of(rental2));
    }

    @Test
    void getUserByIdTest() throws Exception{
        int id = users.get(0).getUserId();

        when(userRepository.findById(id)).thenReturn(Optional.of(users.get(0)));

        mockMvc.perform(get("/user/get/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(id))
                .andExpect(jsonPath("$.firstName").value(users.get(0).getFirstName()))
                .andExpect(jsonPath("$.lastName").value(users.get(0).getLastName()));
    }

    @Test
    void getAllUsersTest() throws Exception {
        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/user/getAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(users.size())))
                .andExpect(jsonPath("$[0].userId").value(users.get(0).getUserId()))
                .andExpect(jsonPath("$[0].firstName").value(users.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(users.get(0).getLastName()))
                .andExpect(jsonPath("$[1].userId").value(users.get(1).getUserId()))
                .andExpect(jsonPath("$[2].userId").value(users.get(2).getUserId()));
    }

    @Test
    void getAllFilmsByUserTest() throws Exception {
        User user = users.get(0);
        int id = user.getUserId();

        List<Film> filmList = new ArrayList<>();

        for(Rental rental : user.getRentalSet()){
            filmList.add(rental.getRentalInventory().getInventoryFilm());
        }

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/getAllFilms/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(filmList.size())))
                .andExpect(jsonPath("$[0].filmId").value(filmList.get(0).getFilmId()))
                .andExpect(jsonPath("$[0].title").value(filmList.get(0).getTitle()));
    }

    @Test
    void updateUserTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User updatedUser = users.get(0);
        updatedUser.setFirstName("Alex");
        String userBody = mapper.writeValueAsString(updatedUser);

        when(userRepository.findById(updatedUser.getUserId())).thenReturn(Optional.of(updatedUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        mockMvc.perform(put("/user/update/" + updatedUser.getUserId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userBody))
                .andExpect(jsonPath("$.userId").value(updatedUser.getUserId()))
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedUser.getLastName()));
    }

    @Test
    void createUserTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User userToCreate = new User("Daisy", "Dale");
        String userBody = mapper.writeValueAsString(userToCreate);

        when(userRepository.save(userToCreate)).thenReturn(userToCreate);

        mockMvc.perform(post("/user/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userBody))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserTest() throws Exception{
        int id = users.get(0).getUserId();
        mockMvc.perform(delete("/user/delete/" + id)).andExpect(status().isOk());
    }
}
