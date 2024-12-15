package sales.sales.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import sales.sales.models.SaleItem;

import java.util.UUID;

public interface SaleItemRepository extends JpaRepository<SaleItem, UUID> {

}
