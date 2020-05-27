package com.tommy.fsd.repo;

import com.tommy.fsd.entities.ProductInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInOrderRepo extends JpaRepository<ProductInOrder, Long> {
}
