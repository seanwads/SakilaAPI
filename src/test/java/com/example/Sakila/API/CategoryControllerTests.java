package com.example.Sakila.API;

import com.example.Sakila.API.Controller.CategoryController;
import com.example.Sakila.API.Model.Category;
import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Repository.CatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CatRepository catRepository;

    static List<Category> categories = new ArrayList<>();
    static Film film;

    @BeforeAll
    static void setTestData(){
        categories.addAll(List.of(
                new Category("Action"),
                new Category("Horror"),
                new Category("Comedy")
        ));
    }

    @Test
    void getCategoryTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        int id = categories.get(0).getCategoryId();

        when(catRepository.findById(id)).thenReturn(Optional.ofNullable(categories.get(0)));

        mockMvc.perform(get("/cat/get/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(id))
                .andExpect(jsonPath("$.name").value(categories.get(0).getName()));
    }

    @Test
    void getCategoryByNameTest() throws Exception {
        String name = categories.get(0).getName();

        when(catRepository.findByName(name)).thenReturn(Optional.of(categories.get(0)));

        mockMvc.perform(get("/cat/getByName/" + name).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(categories.get(0).getCategoryId()))
                .andExpect(jsonPath("$.name").value(categories.get(0).getName()));
    }

    @Test
    void getAllCategories() throws Exception {
        when(catRepository.findAll()).thenReturn(categories);

        mockMvc.perform(get("/cat/getAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(categories.size())))
                .andExpect(jsonPath("$[0].categoryId").value(categories.get(0).getCategoryId()))
                .andExpect(jsonPath("$[0].name").value(categories.get(0).getName()))
                .andExpect(jsonPath("$[1].categoryId").value(categories.get(1).getCategoryId()))
                .andExpect(jsonPath("$[2].categoryId").value(categories.get(2).getCategoryId()));
    }

    @Test
    void updateCategoryTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Category updatedCat = categories.get(0);
        updatedCat.setName("Fantasy");
        String catBody = mapper.writeValueAsString(updatedCat);

        when(catRepository.findById(updatedCat.getCategoryId())).thenReturn(Optional.of(updatedCat));
        when(catRepository.save(updatedCat)).thenReturn(updatedCat);

        mockMvc.perform(put("/cat/update/" + updatedCat.getCategoryId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(catBody))
                .andExpect(jsonPath("$.categoryId").value(updatedCat.getCategoryId()))
                .andExpect(jsonPath("$.name").value(updatedCat.getName()));
    }

    @Test
    void createCategoryTestSuccessful() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Category catToCreate = new Category("Documentary");
        String catBody = mapper.writeValueAsString(catToCreate);

        when(catRepository.findByName(catToCreate.getName())).thenReturn(Optional.empty());
        when(catRepository.save(catToCreate)).thenReturn(catToCreate);

        mockMvc.perform(post("/cat/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(catBody))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCategoryTest() throws Exception {
        int id = categories.get(0).getCategoryId();
        mockMvc.perform(delete("/cat/delete/" + id)).andExpect(status().isOk());
    }
}
