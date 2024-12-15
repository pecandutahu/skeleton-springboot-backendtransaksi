package sales.sales.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import sales.sales.models.Sale;
import sales.sales.models.SaleItem;
import sales.sales.models.User;
import sales.sales.services.ProductService;
import sales.sales.services.SaleService;

@RestController
@RequestMapping("/api/sales")
@Validated

public class SaleController {
    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public WebResponse<List<Sale>> getAllSales(User user) {
        return WebResponse.<List<Sale>>builder().data(saleService.getAllSales()).messages("success").build();
    }

    @GetMapping("/{id}")
    public WebResponse<Sale> getSaleById(User user, @PathVariable UUID id) {
        Optional<Sale> sale = saleService.getSaleById(id);
        return WebResponse.<Sale>builder().data(sale.get()).messages("success").build();
    }

    @PostMapping
    public WebResponse<Sale> createSale(User user, @Valid @RequestBody Sale sale) throws MethodArgumentNotValidException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(sale, "sale");

        // Validasi subtotal dan total price
        double calculatedSubtotal = sale.getSaleItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        if (calculatedSubtotal != sale.getTotalPrice()) {
            bindingResult.addError(new FieldError("sale", "totalPrice",
                    "Total price does not match the sum of sale item subtotals"));
        }

        // Validasi setiap item
        for (int i = 0; i < sale.getSaleItems().size(); i++) {
            SaleItem item = sale.getSaleItems().get(i);
            Optional<Product> product = productService.getProductById(item.getProduct().getProductId());
            if (product.isPresent()) {
                if (product.get().getStock() < item.getQuantity()) {
                    bindingResult.addError(new FieldError("sale", "saleItems[" + i + "].stok",
                    "Out of stock"));
                }
                
                double productPrice = product.get().getPrice();
                if (item.getPrice() != productPrice) {
                    bindingResult.addError(new FieldError("sale", "saleItems[" + i + "].price",
                            "Price does not match the product price for Product " + item.getProduct().getProductName()));
                }
            } else {
                bindingResult.addError(new FieldError("sale", "saleItems[" + i + "].productId",
                "Product not found"));
            }
        }

        // Jika ada error, lempar MethodArgumentNotValidException
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        sale.setCreatedBy(user.getUserId());
        return WebResponse.<Sale>builder().data(saleService.save(sale)).messages("success").build();
    }

    @PutMapping("/refund/{id}")
    public WebResponse<Sale> updateSale(User user, @PathVariable UUID id, @Valid @RequestBody Sale saleDetails) {
        Optional<Sale> sale = saleService.getSaleById(id);
        if (sale.get().getStatus().equals("refund")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This sales has been refunded before");
        }
        sale.get().setUpdatedBy(user.getUserId());
        sale.get().setStatus("refund");
        saleService.save(sale.get());
        return WebResponse.<Sale>builder().data(sale.get()).messages("Success").build();
    }

    @DeleteMapping("/{id}")
    public WebResponse<Sale> deleteCustomer(User user,@PathVariable UUID id) {
        Sale sale = saleService.getSaleById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data Not Found"));
        saleService.deleteById(id, user);
        return WebResponse.<Sale>builder()
                .data(sale)
                .messages("Data deleted successfully")
                .build();
    }

}
