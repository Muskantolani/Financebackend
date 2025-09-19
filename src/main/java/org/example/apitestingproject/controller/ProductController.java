//package org.example.apitestingproject.controller;
//
//import jakarta.validation.Valid;
//import org.example.apitestingproject.entities.Product;
//import org.example.apitestingproject.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URI;
//
//@RestController
//@RequestMapping("/products")
//public class ProductController {
//
////    private final ProductService service; // constructor injection
////
////    public ProductController(ProductService service) {
////        this.service = service;
////    }
//
//    @Autowired
//    ProductService service;
//
//    @PostMapping
//    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
//        Product saved = service.create(product);
//        return ResponseEntity
//                .created(URI.create("/products/" + saved.getId()))
//                .body(saved);
//    }
//
//    @GetMapping("/products/{id}")
//    public ResponseEntity<Product> get(@PathVariable int id) {
//        return service.get(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @GetMapping
//    public ResponseEntity<Iterable<Product>> list() {
//        return ResponseEntity.ok(service.list());
//    } //to check
//
////    @GetMapping("/products/search")
////    public ResponseEntity<?> search(@RequestParam String prefix) {
////        return ResponseEntity.ok(service.searchByNamePrefix(prefix));
////    }
//
//    @GetMapping("/products/search/{prefix}")
//    public ResponseEntity<?> search(@PathVariable String prefix) {
//        return ResponseEntity.ok(service.searchByNamePrefix(prefix));
//    }
//
//    @PutMapping("/products/{id}")
//    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product product) {
//        product.setId(id);
//        return service.get(id)
//                .map(p -> ResponseEntity.ok(service.update(product)))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/products/{id}")
//    public ResponseEntity<Void> delete(@PathVariable int id) {
//        return service.get(id)
//                .map((Product p) -> {
//                    service.delete(id);
//                    return ResponseEntity.noContent().<Void>build();  // explicit generic
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//
////    Get Products by Price Range (filter)
////    Product exist
////    Product Sorting
//
//}
