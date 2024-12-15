package sales.sales.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityManager;
import sales.sales.models.Product;
import sales.sales.models.Sale;
import sales.sales.models.SaleItem;
import sales.sales.models.User;
import sales.sales.repositories.ProductRepository;
import sales.sales.repositories.SaleItemRepository;
import sales.sales.repositories.SaleRepository;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    public List<SaleItem> getAllSaleItem() {
        return saleItemRepository.findAll();
    }

    public Optional<SaleItem> getSaleById(UUID id) {
        entityManager.unwrap(Session.class).enableFilter("deletedSaleFilter");
        return saleItemRepository.findById(id);
    }

    public SaleItem save(SaleItem sale) {
        return saleItemRepository.save(sale);
    }

    public void deleteById(UUID id, User user) {
        entityManager.unwrap(Session.class).enableFilter("deletedSaleFilter");
        SaleItem saleItem = saleItemRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale not found"));
            saleItem.setDeleted(true);
            saleItem.setUpdatedBy(user.getUserId());
            saleItemRepository.save(saleItem);
    }
}
