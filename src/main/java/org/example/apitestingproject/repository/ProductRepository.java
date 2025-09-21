package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByProductNameStartingWithIgnoreCase(String name);
    List<Product> findByCategory(String name);
}
