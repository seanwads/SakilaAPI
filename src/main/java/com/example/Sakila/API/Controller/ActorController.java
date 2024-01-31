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

    private final ActorRepository actorRepository;
    private static final String notFoundResponse = "Actor not found";

    @Autowired
    public ActorController(ActorRepository actorRepository){
        this.actorRepository = actorRepository;
    }

    @GetMapping("/get/{id}")
    public Actor getActorById(@PathVariable("id") int actorId){
        return actorRepository.findById(actorId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));
    }

    @GetMapping("/getAll")
    public Iterable<Actor> getActors(){
        return actorRepository.findAll();
    }

    @GetMapping("/getByFilmId/{id}")
    public Iterable<Actor> getActorsByFilmId(@PathVariable("id") int filmId){
        return actorRepository.findByFilmId(filmId);
    }

    @GetMapping("/getByFilmTitle/{title}")
    public Iterable<Actor> getActorsByFilmTitle(@PathVariable("title") String filmTitle){
        return actorRepository.findByFilmTitle(filmTitle);
    }

    @PutMapping("/update/{id}")
    public Actor updateActorById(
            @PathVariable("id") int actorId,
            @RequestBody Actor updatedActor
    ){
        Actor actor = actorRepository.findById(actorId).orElseThrow(() -> new ResourceAccessException(notFoundResponse));

        actor.setFirstName(updatedActor.getFirstName());
        actor.setLastName(updatedActor.getLastName());
        actor.setFilmsActedIn(updatedActor.getFilmsActedIn());

        return actorRepository.save(actor);
    }

    @PostMapping("/create")
    public Actor createActor(@RequestBody Actor newActor){
        return actorRepository.save(newActor);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteActorById(@PathVariable("id") int actorId){
        actorRepository.deleteById(actorId);
    }
}
