package com.example.Sakila.API.Model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rental")
public class Rental {
    @Id
    @Column(name ="rental_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rentalId;

    @Column(name = "rental_date")
    @Temporal(TemporalType.DATE)
    private Date rentalDate;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory rentalInventory;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer rentalCustomer;

    public Rental(){

    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Inventory getRentalInventory() {
        return rentalInventory;
    }

    public void setRentalInventory(Inventory rentalInventory) {
        this.rentalInventory = rentalInventory;
    }

    public Customer getRentalCustomer() {
        return rentalCustomer;
    }

    public void setRentalCustomer(Customer rentalCustomer) {
        this.rentalCustomer = rentalCustomer;
    }
}
