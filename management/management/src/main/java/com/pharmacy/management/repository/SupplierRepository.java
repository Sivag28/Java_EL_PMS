package com.pharmacy.management.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pharmacy.management.models.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
        List<Supplier> findBySupplierName(String name);
    
    List<Supplier> findBySupplierNameContaining(String namePart);
    
    List<Supplier> findByCompanyName(String companyName);
    
    List<Supplier> findBySupplierEmail(String email);
    
    // Custom query example:
    @Query("SELECT s FROM Supplier s WHERE s.drugsSupplied LIKE %:drugName%")
    List<Supplier> findSuppliersByDrugSupplied(@Param("drugName") String drugName);
}
