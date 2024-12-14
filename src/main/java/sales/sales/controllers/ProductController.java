package sales.sales.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import sales.sales.dto.WebResponse;
import sales.sales.models.Product;
import sales.sales.models.User;
import sales.sales.services.ProductService;

@RestController
@RequestMapping("/api/products")
@Validated

public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public WebResponse<List<Product>> getAllProducts(User user) {
        return WebResponse.<List<Product>>builder().data(productService.getAllProducts()).messages("success").build();
    }

    @GetMapping("/{id}")
    public WebResponse<Product> getProductById(User user, @PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return WebResponse.<Product>builder().data(product.get()).messages("success").build();
    }

    @PostMapping
    public WebResponse<Product> createProduct(User user, @Valid @RequestBody Product product) {
        product.setCreatedBy(user.getUserId());
        return WebResponse.<Product>builder().data(productService.save(product)).messages("success").build();
    }

    @PutMapping("/{id}")
    public WebResponse<Product> updateProduct(User user, @PathVariable Long id, @Valid @RequestBody Product productDetails) {
        Optional<Product> product = productService.getProductById(id);
        product.get().setProductName(productDetails.getProductName());
        product.get().setPrice(productDetails.getPrice());
        product.get().setStock(productDetails.getStock());
        product.get().setUpdatedBy(user.getUserId());
        productService.save(product.get());
        return WebResponse.<Product>builder().data(product.get()).messages("Success").build();
    }

    @DeleteMapping("/{id}")
    public WebResponse<Product> deleteCustomer(User user,@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data Not Found"));
        productService.deleteById(id, user);
        return WebResponse.<Product>builder()
                .data(product)
                .messages("Data deleted successfully")
                .build();
    }

}
