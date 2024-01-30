package com.example.Sakila.API.Controller;

import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Model.Rental;
import com.example.Sakila.API.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Sakila.API.Model.User;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/getAll")
    private Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/getAllFilms/{id}")
    private List<Film> getAllFilmsByUserId(@PathVariable("id") int userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceAccessException("User not found"));

        List<Film> films = new ArrayList<>();
        for(Rental rental : user.getRentalSet()){
            films.add(rental.getRentalInventory().getInventoryFilm());
        }

        return films;
    }

    @PutMapping("/update/{id}")
    private User updateUserById(
            @PathVariable("id") int userId,
            @RequestBody User updatedUser
    ){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceAccessException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setRentalSet(updatedUser.getRentalSet());
        user.setEmailAddress(updatedUser.getEmailAddress());
        user.setActive(updatedUser.getActive());

        return userRepository.save(user);
    }

    @PostMapping("/create")
    private User createUser(@RequestBody User newUser){
        return userRepository.save(newUser);
    }

    @DeleteMapping("/delete/{id}")
    private void deleteUserById(@PathVariable("id") int userId){
        userRepository.deleteById(userId);
    }
}
