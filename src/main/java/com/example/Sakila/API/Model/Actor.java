package com.example.Sakila.API.Model;
import jakarta.persistence.*;

@Entity
@Table(name="actor")
public class Actor {
    @Id
    @Column(name = "actor_id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int actorId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public Actor(){}

    public Actor(int id, String first, String last){
        this.actorId = id;
        this.firstName = first;
        this.lastName = last;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
