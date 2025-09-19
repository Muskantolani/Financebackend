package org.example.apitestingproject;

import org.example.apitestingproject.entities.Product;
import org.example.apitestingproject.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void clearDb() {
        productRepository.deleteAll();
    }

    @Test
    void seedProductsAcrossCategories_andVerifyQueries() {
        List<Product> seed = List.of(
                // Electronics (<= 2L for EMI)
                make("MacBook Pro 14\" M3",        "Apple laptop, 16GB/512GB",  bd(185000), 5,  "Electronics"),
                make("Samsung Galaxy S24 Ultra",   "Android flagship 256GB",    bd(130000), 10, "Electronics"),
                make("Sony Bravia 55\" OLED",      "4K OLED Google TV",         bd(150000), 8,  "Electronics"),
                make("Bose Soundbar 900",          "Dolby Atmos soundbar",      bd( 90000), 12, "Electronics"),

                // Furniture
                make("Queen Bed with Storage",     "Engineered wood, walnut",   bd( 70000), 9,  "Furniture"),
                make("3-Seater Sofa",              "Fabric, dark grey",         bd( 85000), 5,  "Furniture"),
                make("Dining Table 6-Seater",      "Sheesham wood",             bd( 65000), 7,  "Furniture"),
                make("Ergonomic Office Chair",     "Mesh, adjustable",          bd( 15000), 20, "Furniture"),

                // Automobile (two-wheelers)
                make("Honda Activa 125",           "Scooter (EMI model)",       bd( 90000), 5,  "Automobile"),
                make("TVS iQube Electric",         "E-scooter (EMI model)",     bd(120000), 7,  "Automobile"),
                make("Royal Enfield Classic 350",  "Motorcycle (partial EMI)",  bd(185000), 4,  "Automobile"),
                make("Ather 450X",                 "Electric scooter",          bd(140000), 6,  "Automobile"),

                // Home Appliances
                make("LG 600L Refrigerator",       "Side-by-side inverter",     bd(140000), 6,  "Home Appliances"),
                make("IFB 8kg Washer",             "Front-load, 1400 RPM",      bd( 60000), 15, "Home Appliances"),
                make("Dyson Air Purifier",         "HEPA H13 filtration",       bd( 55000), 20, "Home Appliances"),
                make("Philips Coffee Machine",     "Espresso + milk frother",   bd( 35000), 10, "Home Appliances")
        );

        productRepository.saveAll(seed);

        // Verify total count
        assertThat(productRepository.count()).isEqualTo(16);

        // Verify category counts
        assertThat(productRepository.findByCategory("Electronics")).hasSize(4);
        assertThat(productRepository.findByCategory("Furniture")).hasSize(4);
        assertThat(productRepository.findByCategory("Automobile")).hasSize(4);
        assertThat(productRepository.findByCategory("Home Appliances")).hasSize(4);

        // EMI rule: Electronics should all be <= 2,00,000
        var electronics = productRepository.findByCategory("Electronics");
        assertThat(electronics).allMatch(p -> p.getCost().compareTo(bd(200000)) <= 0);


    }

    // --- helper methods ---
    private static Product make(String name, String details, BigDecimal price, int stock, String category) {
        Product p = new Product();
        p.setProductName(name);
        p.setProductDetails(details);
        p.setCost(price);
        p.setProductStock(stock);
        p.setCategory(category);
        return p;
    }

    private static BigDecimal bd(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);
    }
}
