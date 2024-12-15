package sales.sales.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import sales.sales.models.Sale;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
    
}
