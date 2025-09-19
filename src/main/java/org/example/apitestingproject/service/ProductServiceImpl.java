//package org.example.apitestingproject.service;
//
//import org.example.apitestingproject.entities.Product;
//import org.example.apitestingproject.repository.ProductRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class ProductServiceImpl implements ProductService {
//
//    private final ProductRepository repo;
//
//    public ProductServiceImpl(ProductRepository repo) {
//        this.repo = repo;
//    }
//
//    @Override
//    public Product create(Product p) {
//        return repo.save(p);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<Product> get(int id) {
//        return repo.findById(id);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<Product> searchByNamePrefix(String prefix) {
//        return repo.findByNameStartingWith(prefix);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Iterable<Product> list() {
//        return repo.findAll();
//    }
//
//    @Override
//    public Product update(Product p) {
//        return repo.save(p);
//    }
//
//    @Override
//    public void delete(int id) {
//        repo.deleteById(id);
//    }
//}
