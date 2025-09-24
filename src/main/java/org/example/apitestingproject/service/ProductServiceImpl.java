package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.Product;
import org.example.apitestingproject.repository.ProductRepository;
import org.example.apitestingproject.service.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Product create(Product p) { return repo.save(p); }

    @Override @Transactional(readOnly = true)
    public Optional<Product> get(int id) { return repo.findById(id); }

    @Override @Transactional(readOnly = true)
    public List<Product> searchByNamePrefix(String prefix) {
        return repo.findByProductNameStartingWithIgnoreCase(prefix);
    }

    @Override @Transactional(readOnly = true)
    public Iterable<Product> list() { return repo.findAll(); }

    @Override
    public Product update(Product p) { return repo.save(p); }

    @Override
    public void delete(int id) { repo.deleteById(id); }

    @Override @Transactional(readOnly = true)
    public List<Product> list(Sort sort) { return repo.findAll(sort); }

    @Override @Transactional(readOnly = true)
    public List<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return repo.findByCostBetween(minPrice, maxPrice);
    }

    @Override @Transactional(readOnly = true)
    public List<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Sort sort) {
        return repo.findByCostBetween(minPrice, maxPrice, sort);
    }

    @Override @Transactional(readOnly = true)
    public List<Product> filterByCategoryAndPrice(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        // push all filtering to DB, case-insensitive
        return repo.findByCategoryIgnoreCaseAndCostBetween(category, minPrice, maxPrice,Sort.unsorted());
    }

    @Override @Transactional(readOnly = true)
    public List<Product> filterByCategoryAndPrice(String category, BigDecimal minPrice, BigDecimal maxPrice, Sort sort) {
        return repo.findByCategoryIgnoreCaseAndCostBetween(category, minPrice, maxPrice, sort);
    }

    @Override @Transactional(readOnly = true)
    public List<Product> listByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category);
    }

    @Override @Transactional(readOnly = true)
    public List<Product> listByCategory(String category, Sort sort) {
        return repo.findByCategoryIgnoreCase(category, sort);
    }

    @Override
    public void adjustStock(int productId, int delta, String reason) {
        Product p = repo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        int newStock = (p.getProductStock() == null ? 0 : p.getProductStock()) + delta;
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock for product " + p.getProductName());
        }
        p.setProductStock(newStock);
        repo.save(p);

        // TODO: persist stock adjustment audit (productId, delta, reason, who, when)
    }

    @Override
    public void setStock(int productId, int newQty) {
        if (newQty < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        Product p = repo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        p.setProductStock(newQty);
        repo.save(p);
    }
}

