package com.pharmacy.management.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Supplier")
public class Supplier {
    @Id
    @Column(name = "supplierid")  // Must match your DB column name exactly
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supplierID;


    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<Stock> stocks;  // Correct relationship mapping
   @Column(name = "quantity", nullable = false, columnDefinition = "integer default 0")
    private Integer quantity = 0;
    private String supplierName;
    private String supplierEmail;
    private String supplierContact;
    private String supplierAddress;
    private String companyName;
    private String contactPerson;
    private String drugsSupplied;
    private Integer totalQuantitySupplied;  // Add this field

    // Correct getters/setters using Integer
    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	public String getSupplierContact() {
		return supplierContact;
	}

	public void setSupplierContact(String supplierContact) {
		this.supplierContact = supplierContact;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getDrugsSupplied() {
		return drugsSupplied;
	}

	public void setDrugsSupplied(String drugsSupplied) {
		this.drugsSupplied = drugsSupplied;
	}

    public Object getCreatedAt() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCreatedAt'");
    }

    
	public Integer getTotalQuantitySupplied() {
        return totalQuantitySupplied;
    }

    public void setTotalQuantitySupplied(Integer totalQuantity) {
        this.totalQuantitySupplied = totalQuantity;
    }
    
    


}
