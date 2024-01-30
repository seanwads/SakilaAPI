package com.example.Sakila.API.Controller;

import com.example.Sakila.API.Model.Category;
import com.example.Sakila.API.Repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/cat")
@RestController
public class CategoryController {
    @Autowired
    private CatRepository catRepository;

    @GetMapping("/get/{id}")
    private Category getCatById(@PathVariable("id") int catId){
        return catRepository.findById(catId).orElse
    }
}
