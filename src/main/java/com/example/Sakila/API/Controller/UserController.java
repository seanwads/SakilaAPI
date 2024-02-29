package com.example.Sakila.API.Controller;

import com.example.Sakila.API.Model.Film;
import com.example.Sakila.API.Model.Inventory;
import com.example.Sakila.API.Model.Rental;
import com.example.Sakila.API.Repository.FilmRepository;
import com.example.Sakila.API.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Sakila.API.Model.User;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;
    private static final String notFoundResponse = "User not found";

    @Autowired
    public UserController(UserRepository userRepository, FilmRepository filmRepository){
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
    }

    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable("id") int userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
    }

    @GetMapping("/getAll")
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/getAllFilms/{id}")
    public List<Film> getAllFilmsByUserId(@PathVariable("id") int userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));

        List<Film> films = new ArrayList<>();
        for(Rental rental : user.getRentalSet()){
            films.add(rental.getRentalInventory().getInventoryFilm());
        }

        return films;
    }

    @PutMapping("/update/{id}")
    public User updateUserById(
            @PathVariable("id") int userId,
            @RequestBody User updatedUser
    ){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setRentalSet(updatedUser.getRentalSet());
        user.setEmailAddress(updatedUser.getEmailAddress());
        user.setActive(updatedUser.getActive());

        return userRepository.save(user);
    }

    @PutMapping("/addFilm/{userId}/{filmId}")
    public User addFilmToUser(@PathVariable("userId") int userId, @PathVariable("filmId") int filmId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new ResourceAccessException("Film not found"));

        for(Rental rental : user.getRentalSet()){
            if(rental.getRentalInventory().getInventoryFilm().getFilmId() == filmId){
                return user;
            }
        }

        Inventory inventory = new Inventory();
        Rental rental = new Rental();

        inventory.setInventoryFilm(film);
        rental.setRentalInventory(inventory);
        rental.setRentalCustomer(user);

        Set<Rental> userFilms = new HashSet<>(user.getRentalSet());
        userFilms.add(rental);
        user.setRentalSet(userFilms);

        return userRepository.save(user);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User newUser){
        return userRepository.save(newUser);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable("id") int userId){
        userRepository.deleteById(userId);
    }
}
