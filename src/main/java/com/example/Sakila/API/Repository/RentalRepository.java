package com.example.Sakila.API.Repository;

import com.example.Sakila.API.Model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
}
