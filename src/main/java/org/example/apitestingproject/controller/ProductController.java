package org.example.apitestingproject.controller;

import org.example.apitestingproject.entities.Product;
import org.example.apitestingproject.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.create(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // READ single product
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Optional<Product> product = productService.get(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // READ all products
    @GetMapping
    public ResponseEntity<Iterable<Product>> listProducts() {
        Iterable<Product> products = productService.list();
        return ResponseEntity.ok(products);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Optional<Product> existing = productService.get(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        product.setId(id); // ensure the ID is set
        Product updated = productService.update(product);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        Optional<Product> existing = productService.get(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
