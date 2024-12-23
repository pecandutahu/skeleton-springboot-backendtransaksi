package sales.sales.services;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import sales.sales.models.Product;
import sales.sales.models.Sale;
import sales.sales.models.SaleItem;
import sales.sales.models.User;
import sales.sales.repositories.ProductRepository;
import sales.sales.repositories.SaleItemRepository;
import sales.sales.repositories.SaleRepository;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemService saleItemService;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Sale> getAllSales() {
        entityManager.unwrap(Session.class).enableFilter("deletedSaleFilter");
        return saleRepository.findAll();
    }

    public List<Sale> getSalesReport(String startDate, String endDate, String status) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = startDate != null ? 
                LocalDate.parse(startDate, dateFormatter).atStartOfDay() : null; // Jam default: 00:00:00
        LocalDateTime end = endDate != null ? 
                LocalDate.parse(endDate, dateFormatter).atTime(23, 59, 59) : null; // Jam default: 23:59:59

    
        entityManager.unwrap(Session.class).enableFilter("deletedSaleFilter");
        return saleRepository.findAll().stream()
            .filter(sale -> (startDate == null || sale.getSaleDate().isAfter(start)) && 
                            (endDate == null || sale.getSaleDate().isBefore(end)) && 
                            (status == null || sale.getStatus().equals(status)))
            .toList();
    }

    public Optional<Sale> getSaleById(UUID id) {
        entityManager.unwrap(Session.class).enableFilter("deletedSaleFilter");
        Optional<Sale> sale = saleRepository.findById(id);
        if (!sale.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale not found");
        }
        return sale;
    }

    @Transactional
    public Sale save(Sale sale) {

        Sale savedSale = saleRepository.save(sale);
        for (SaleItem saleItem : sale.getSaleItems()) {
            saleItem.setSaleId(savedSale.getSaleId());
            saleItem.setCreatedBy(savedSale.getCreatedBy());
            saleItemService.save(saleItem);

            // Reduce Stok Blok
            Optional<Product> product = productRepository.findById(saleItem.getProduct().getProductId());
            if (product.isPresent()) {
                if(sale.getStatus().equals("refund")) {
                    product.get().setStock(product.get().getStock() + saleItem.getQuantity());
                }else {
                    product.get().setStock(product.get().getStock() - saleItem.getQuantity());
                }
                productRepository.save(product.get());
            }
        }
        return savedSale;
    }

    public void deleteById(UUID id, User user) {
        entityManager.unwrap(Session.class).enableFilter("deletedSaleFilter");
        Sale sale = saleRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale not found"));
            sale.setDeleted(true);
            sale.setUpdatedBy(user.getUserId());
            saleRepository.save(sale);
    }
}
