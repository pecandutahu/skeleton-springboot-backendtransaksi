package sales.sales.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityManager;
import sales.sales.models.Product;
import sales.sales.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Product> getAllProducts() {
        entityManager.unwrap(Session.class).enableFilter("deletedProductFilter");
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        entityManager.unwrap(Session.class).enableFilter("deletedProductFilter");
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        entityManager.unwrap(Session.class).enableFilter("deletedProductFilter");
        Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found for this id :: " + id));
            product.setDeleted(true);
            productRepository.save(product);
    }
}
