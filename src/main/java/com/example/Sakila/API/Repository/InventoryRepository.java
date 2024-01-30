package com.example.Sakila.API.Repository;

import com.example.Sakila.API.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
