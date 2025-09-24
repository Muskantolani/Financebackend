package org.example.apitestingproject.controller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import org.apache.coyote.BadRequestException;
import org.example.apitestingproject.dto.ProductCreateRequest;
import org.example.apitestingproject.dto.ProductAdminDisplayDTO;
import org.example.apitestingproject.dto.ProductUpdateRequest;
import org.example.apitestingproject.dto.StockUpdateRequest;
import org.example.apitestingproject.entities.Product;
import org.example.apitestingproject.mapper.ProductMapper;
import org.example.apitestingproject.service.ProductService;
import org.example.apitestingproject.service.exceptions.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin/products")
@Validated
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','SUB_ADMIN')")
public class ProductControllerAdmin {

    private final ProductService productService;

    public ProductControllerAdmin(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping
    public List<ProductAdminDisplayDTO> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal minPrice,
            @RequestParam(required = false) @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal maxPrice
    ) {
        List<Product> products;
        if (minPrice != null && maxPrice != null && category != null && !category.isBlank()) {
            products = productService.filterByCategoryAndPrice(category, minPrice, maxPrice);
        } else if (minPrice != null && maxPrice != null) {
            products = productService.filterByPriceRange(minPrice, maxPrice);
        } else {
            products = (List<Product>) productService.list();
        }
        return products.stream().map(ProductMapper::toDTO).toList();
    }

    @PostMapping
    public ProductAdminDisplayDTO create(@Valid @RequestBody ProductCreateRequest req) {
        Product p = ProductMapper.fromCreate(req);
        Product saved = productService.create(p);
        return ProductMapper.toDTO(saved);
    }

    @PutMapping("/{id}")
    public ProductAdminDisplayDTO update(@PathVariable int id, @Valid @RequestBody ProductUpdateRequest req) {
        Product existing = productService.get(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        ProductMapper.applyUpdate(existing, req);
        Product saved = productService.update(existing);
        return ProductMapper.toDTO(saved);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        productService.delete(id);
    }

    @PatchMapping("/{id}/stock")
    public ProductAdminDisplayDTO updateStock(
            @PathVariable int id,
            @RequestBody StockUpdateRequest req
    ) throws BadRequestException {

        if (req.getNewQty() == null && req.getDelta() == null) {
            throw new BadRequestException("Provide either newQty or delta");
        }

        // If using delta, a reason is required
        if (req.getDelta() != null) {
            if (req.getReason() == null || req.getReason().isBlank()) {
                throw new BadRequestException("Reason is required when using delta");
            }
            productService.adjustStock(id, req.getDelta(), req.getReason());
        } else {
            productService.setStock(id, req.getNewQty());
        }

        return productService.get(id)
                .map(ProductMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found after update: " + id));
    }


}

