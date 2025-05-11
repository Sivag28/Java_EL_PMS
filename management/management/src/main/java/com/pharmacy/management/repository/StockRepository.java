package com.pharmacy.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import com.pharmacy.management.models.Stock;
import com.pharmacy.management.models.Supplier;

public interface StockRepository extends JpaRepositoryImplementation<Stock, Integer> {
	@Query("SELECT s FROM Stock s WHERE s.supplier.supplierID = :supplierId")
    List<Stock> findBySupplierId(@Param("supplierId") Integer supplierId);
    @Override
    List<Stock> findAll();

    List<Stock> findBySupplier(Supplier supplier);

}