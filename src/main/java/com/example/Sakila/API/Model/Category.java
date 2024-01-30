package com.example.Sakila.API.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(name = "name")
    private String name;

    public Set<Film> getFilmSet() {
        return filmSet;
    }

    public void setFilmSet(Set<Film> filmSet) {
        this.filmSet = filmSet;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "categorySet")
    Set<Film> filmSet;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
