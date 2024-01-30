package com.example.Sakila.API.Model;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @Column(name = "inventory_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inventoryId;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film inventoryFilm;

    @OneToMany(mappedBy = "rentalInventory")
    private Set<Rental> rentalSet;

    public Inventory(){

    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Film getInventoryFilm() {
        return inventoryFilm;
    }

    public void setInventoryFilm(Film inventoryFilm) {
        this.inventoryFilm = inventoryFilm;
    }
}
