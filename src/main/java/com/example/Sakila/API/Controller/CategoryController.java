package com.example.Sakila.API.Controller;

import com.example.Sakila.API.Model.Category;
import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RequestMapping("/cat")
@RestController
public class CategoryController {
    @Autowired
    private CatRepository catRepository;

    @GetMapping("/get/{id}")
    private Category getCatById(@PathVariable("id") int catId){
        return catRepository.findById(catId).orElseThrow(() -> new ResourceAccessException("Category not found"));
    }

    @GetMapping("/getAll")
    private Iterable<Category> getAllCats(){
        return catRepository.findAll();
    }

    @GetMapping("/getFilmsByCat/{id}")
    private List<Film> getFilmsByCat(@PathVariable("id") int catId){
        Category cat = catRepository.findById(catId).orElseThrow(() -> new ResourceAccessException("Category not found"));
        return new ArrayList<>(cat.getFilmSet());
    }

    @PutMapping("/update/{id}")
    private Category updateCatById(
            @PathVariable("id") int catId,
            @RequestBody Category updatedCategory){
        Category cat = catRepository.findById(catId).orElseThrow(() -> new ResourceAccessException("Category not found"));
        cat.setName(updatedCategory.getName());
        return catRepository.save(cat);
    }

    @PostMapping("/create")
    private Category createCat(@RequestBody Category newCat){
        return catRepository.save(newCat);
    }

    @DeleteMapping("/delete/{id}")
    private void deleteCatById(@PathVariable("id") int catId){
        catRepository.deleteById(catId);
    }
}
