package org.example.apitestingproject.controller;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import org.apache.coyote.BadRequestException;
import org.example.apitestingproject.dto.ProductAdminDisplayDTO;
import org.example.apitestingproject.entities.Product;
import org.example.apitestingproject.mapper.ProductMapper;
import org.example.apitestingproject.service.ProductService;
import org.example.apitestingproject.service.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173","http://127.0.0.1:5173"}, allowedHeaders="*", methods = {RequestMethod.GET, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/user/products")
@Validated
public class ProductControllerUser {

    private final ProductService productService;

    public ProductControllerUser(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductAdminDisplayDTO> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal minPrice,
            @RequestParam(required = false) @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal maxPrice,
            @RequestParam(defaultValue = "productName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) throws BadRequestException {
        if ((minPrice != null && maxPrice == null) || (minPrice == null && maxPrice != null)) {
            throw new BadRequestException("Provide both minPrice and maxPrice together.");
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new BadRequestException("minPrice cannot be greater than maxPrice.");
        }

        // normalize and validate sort field
        Sort sort = buildSort(sortBy, direction);

        List<Product> products;
        if (category != null && !category.isBlank() && minPrice != null && maxPrice != null) {
            products = productService.filterByCategoryAndPrice(category, minPrice, maxPrice, sort);
        } else if (minPrice != null && maxPrice != null) {
            products = productService.filterByPriceRange(minPrice, maxPrice, sort);
        } else if (category != null && !category.isBlank()) {
            products = productService.listByCategory(category, sort);
        } else {
            products = productService.list(sort);
        }
        return products.stream().map(ProductMapper::toDTO).toList();
    }

    private Sort buildSort(String sortBy, String direction) throws BadRequestException {
        // allowlist of sortable fields to avoid SQL injection / invalid property
        // must match field names on the entity
        switch (sortBy) {
            case "productName":
            case "cost":
            case "productStock":
            case "category":
                break;
            default:
                throw new BadRequestException("Invalid sortBy. Allowed: productName, cost, productStock, category");
        }
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, sortBy);
    }

    @GetMapping("/{id}")
    public ProductAdminDisplayDTO get(@PathVariable int id) {
        Product p = productService.get(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return ProductMapper.toDTO(p);
    }
}

