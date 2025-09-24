package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByProductNameStartingWithIgnoreCase(String name);
    List<Product> findByCategory(String name);
    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findByCategoryStartingWithIgnoreCase(String category);
    List<Product> findByCostBetween(BigDecimal min, BigDecimal max);
    List<Product> findByCategoryAndCostBetween(String category, BigDecimal min, BigDecimal max);
    List<Product> findByCategoryIgnoreCase(String category, Sort sort);
    List<Product> findByCostBetween(BigDecimal min, BigDecimal max, Sort sort);
    List<Product> findByCategoryIgnoreCaseAndCostBetween(String category, BigDecimal min, BigDecimal max, Sort sort);

}
