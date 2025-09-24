package org.example.apitestingproject.controller;

//import org.example.apitestingproject.dto.ProductAdminDisplayDTO;
import org.example.apitestingproject.dto.ProductDTO;
import org.example.apitestingproject.entities.CardType;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.EmiCardRepository;
import org.example.apitestingproject.repository.UserRepository;
import org.example.apitestingproject.service.EmiPreviewService;
import org.example.apitestingproject.service.ProductService;
import org.example.apitestingproject.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/user")
public class UserServicesController {

    private final ProductService productService;
    private final EmiPreviewService emiPreviewService;
    private  final EmiCardRepository emiCardRepository;
    private final PurchaseService purchaseService;
    private final UserRepository userRepository;

    public UserServicesController(ProductService productService, EmiPreviewService emiPreviewService, EmiCardRepository emiCardRepository, PurchaseService purchaseService, UserRepository userRepository) {
        this.productService = productService;
        this.emiPreviewService = emiPreviewService;
        this.emiCardRepository = emiCardRepository;
        this.purchaseService = purchaseService;
        this.userRepository = userRepository;
    }
    @GetMapping("/{userId}/products")
    public ResponseEntity<List<ProductDTO>> getUserPurchasedProducts(@PathVariable int userId) {
        List<ProductDTO> products = purchaseService.getProductsByUser(userId);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getCardTypeFromUserId(@PathVariable int userId) {
        return userRepository.findById(userId)
                .map(user -> {
                       // get card type
                    return ResponseEntity.ok(user.getPreferredCardType()); // or cardType.getName()
                })
                .orElse(ResponseEntity.notFound().build());
    }




}
