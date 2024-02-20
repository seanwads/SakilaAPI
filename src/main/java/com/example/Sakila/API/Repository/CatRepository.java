package com.example.Sakila.API.Repository;
import com.example.Sakila.API.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CatRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT category FROM Category category WHERE category.name LIKE CONCAT('%', :name, '%')")
    Optional<Category> findByName(@Param("name") String name);
}
