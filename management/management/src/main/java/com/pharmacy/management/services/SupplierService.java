package com.pharmacy.management.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.pharmacy.management.models.Stock;
import com.pharmacy.management.models.Supplier;
import com.pharmacy.management.repository.StockRepository;
import com.pharmacy.management.repository.SupplierRepository;

@Service
public class SupplierService {	

	    @Autowired
	    private SupplierRepository repository;
		@Autowired
           private StockRepository stockRepository;
		   @Autowired
	    private SupplierRepository supplierRepository;
	
    public SupplierService() {
    }

	    public List<Supplier> filterSuppliersBySupplierName(String keyword) {
	        List<Supplier> allSuppliers = repository.findAll();
	        return allSuppliers.stream()
	            .filter(supplier -> supplier.getSupplierName().toLowerCase().contains(keyword.toLowerCase()))
	            .collect(Collectors.toList());
	    }
	    
	    @Autowired
	    private JavaMailSender emailSender;
	    
	    
	    public void sendSimpleEmail(Supplier supplier, String drugsReq) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(supplier.getSupplierEmail());
	        
	        String mailBody = "Dear " + supplier.getSupplierName() + ",\n\n" +
	                          "We are contacting you regarding a request for the following Medical drugs:\n" +
	                          drugsReq + "\n\n" +
	                          "Please let us know the availability and pricing for these drugs at your earliest convenience.\n\n" +
	                          "Thank you,\n" +
	                          "+PharmaPlus+";
	        
	        message.setText(mailBody);
	        message.setSubject("Medical Drug Request");
	        emailSender.send(message);
	    }

    public void updateSupplier(Supplier supplier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Supplier> getAllSuppliers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	public List<Supplier> getAllSuppliersWithQuantity() {
		List<Supplier> suppliers = supplierRepository.findAll();
		
		suppliers.forEach(supplier -> {
			int totalQuantity = stockRepository.findBySupplier(supplier)
				.stream()
				.mapToInt(Stock::getQuantity)
				.sum();
			supplier.setTotalQuantitySupplied(totalQuantity);
		});
		
		return suppliers;
	}
	


}

