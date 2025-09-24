package org.example.apitestingproject.mapper;
import org.example.apitestingproject.dto.ProductCreateRequest;
import org.example.apitestingproject.dto.ProductAdminDisplayDTO;
import org.example.apitestingproject.dto.ProductUpdateRequest;
import org.example.apitestingproject.entities.Product;

public final class ProductMapper {

    private ProductMapper() {}

    public static ProductAdminDisplayDTO toDTO(Product p) {
        if (p == null) return null;
        ProductAdminDisplayDTO dto = new ProductAdminDisplayDTO();
        dto.setId(p.getId());
        dto.setProductName(p.getProductName());
        dto.setProductDetails(p.getProductDetails());
        dto.setCost(p.getCost());
        dto.setProductStock(p.getProductStock());
        dto.setCategory(p.getCategory());
        return dto;
    }

    public static Product fromCreate(ProductCreateRequest req) {
        Product p = new Product();
        p.setProductName(req.getProductName());
        p.setProductDetails(req.getProductDetails());
        p.setCost(req.getCost());
        p.setProductStock(req.getProductStock());
        p.setCategory(req.getCategory());
        return p;
    }

    public static void applyUpdate(Product target, ProductUpdateRequest req) {
        target.setProductName(req.getProductName());
        target.setProductDetails(req.getProductDetails());
        target.setCost(req.getCost());
        target.setProductStock(req.getProductStock());
        target.setCategory(req.getCategory());
    }
}

