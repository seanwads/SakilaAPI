package com.example.Sakila.API.Controller;

import com.example.Sakila.API.Model.Actor;
import com.example.Sakila.API.Repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

@CrossOrigin
@RequestMapping("/actor")
@RestController
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;

    @GetMapping("/get/{id}")
    private Actor getActorById(@PathVariable("id") int actorId){
        return actorRepository.findById(actorId).orElseThrow(() -> new ResourceAccessException("Actor not found"));
    }

    @GetMapping("/getAll")
    private Iterable<Actor> getActors(){
        return actorRepository.findAll();
    }

    @GetMapping("/getByFilmId/{id}")
    private Iterable<Actor> getActorsByFilmId(@PathVariable("id") int filmId){
        return actorRepository.findByFilmId(filmId);
    }

    @GetMapping("/getByFilmTitle/{title}")
    private Iterable<Actor> getActorsByFilmTitle(@PathVariable("title") String filmTitle){
        return actorRepository.findByFilmTitle(filmTitle);
    }

    @PutMapping("/update/{id}")
    private Actor updateActorById(
            @PathVariable("id") int actorId,
            @RequestBody Actor updatedActor
    ){
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new ResourceAccessException("Actor not found"));

        actor.setFirstName(updatedActor.getFirstName());
        actor.setLastName(updatedActor.getLastName());
        actor.setFilmsActedIn(updatedActor.getFilmsActedIn());

        return actorRepository.save(actor);
    }

    @PostMapping("/create")
    private Actor createActor(@RequestBody Actor newActor){
        return actorRepository.save(newActor);
    }

    @DeleteMapping("/delete/{id}")
    private void deleteActorById(@PathVariable("id") int actorId){
        actorRepository.deleteById(actorId);
    }
}
