package com.example.Sakila.API.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Year;
import java.util.Set;

@Entity
@Table(name = "film")
public class Film {
    @Id
    @Column(name = "film_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int filmId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "release_year")
    private Year releaseYear;

    @Column(name = "language_id")
    private int languageId;

    @Column(name = "rating")
    private String rating;

    @ManyToMany
    @JoinTable(
            name = "film_category",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<Category> categorySet;

    @OneToMany(mappedBy = "inventoryFilm")
    @JsonIgnore
    private Set<Inventory> filmInventory;

    @ManyToMany(mappedBy = "filmsActedIn")
    private Set<Actor> actors;

    public Film() {
    }

    public Film(int filmId, String title) {
        this.filmId = filmId;
        this.title = title;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Year getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Year releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Set<Category> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(Set<Category> categorySet) {
        this.categorySet = categorySet;
    }

    public Set<Inventory> getFilmInventory() {
        return filmInventory;
    }

    public void setFilmInventory(Set<Inventory> filmInventory) {
        this.filmInventory = filmInventory;
    }

    
}
