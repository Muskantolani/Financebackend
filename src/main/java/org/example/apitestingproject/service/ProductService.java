package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.Product;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product create(Product p);
    Optional<Product> get(int id);
    List<Product> searchByNamePrefix(String prefix);
    Iterable<Product> list();
    Product update(Product p);
    void delete(int id);

    List<Product> list(org.springframework.data.domain.Sort sort);
    List<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Sort sort); // NEW

    List<Product> filterByCategoryAndPrice(String category, BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> filterByCategoryAndPrice(String category, BigDecimal minPrice, BigDecimal maxPrice, Sort sort); // NEW

    List<Product> listByCategory(String category);
    List<Product> listByCategory(String category, Sort sort); // NEW

    void adjustStock(int productId, int delta, String reason);
    void setStock(int productId, int newQty);
}
