package com.example.Sakila.API.Controller;

import com.example.Sakila.API.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Sakila.API.Model.User;
import org.springframework.web.client.ResourceAccessException;

@CrossOrigin
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get/{id}")
    private User getUserById(@PathVariable("id") int userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceAccessException("User not found"));
    }
}
