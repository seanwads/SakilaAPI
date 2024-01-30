package com.example.Sakila.API.Repository;
import com.example.Sakila.API.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Category, Integer> {

}
