package com.example.Sakila.API.Controller;

import com.example.Sakila.API.Model.Category;
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
    private final CatRepository catRepository;
    private static final String notFoundResponse = "Category not found";

    @Autowired
    public CategoryController(CatRepository catRepository){
        this.catRepository = catRepository;
    }

    @GetMapping("/get/{id}")
    public Category getCatById(@PathVariable("id") int catId){
        return catRepository.findById(catId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
    }

    @GetMapping("/getByName/{name}")
    public Category getCatByName(@PathVariable("name") String name){
        return catRepository.findByName(name).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
    }

    @GetMapping("/getAll")
    public Iterable<Category> getAllCats(){
        return catRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public Category updateCatById(
            @PathVariable("id") int catId,
            @RequestBody Category updatedCategory){
        Category cat = catRepository.findById(catId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
        cat.setName(updatedCategory.getName());
        return catRepository.save(cat);
    }

    @PostMapping("/create")
    public Category createCat(@RequestBody Category newCat)
    {
        if(catRepository.findByName(newCat.getName()).isEmpty()){
            return catRepository.save(newCat);
        }
        else {
            throw new IllegalArgumentException("Film already exists");
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCatById(@PathVariable("id") int catId){
        catRepository.deleteById(catId);
    }
}
