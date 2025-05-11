package com.pharmacy.management.contollers;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pharmacy.management.DTO.StockDTO;
import com.pharmacy.management.models.Stock;
import com.pharmacy.management.models.Supplier;
import com.pharmacy.management.repository.StockRepository;
import com.pharmacy.management.repository.SupplierRepository;
import com.pharmacy.management.services.StockService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class StockController {
	
	private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
   
    @GetMapping("/stock/dashboard")
    public String showDashboard(Model model) {
        // Fetch data from your service/repository
        int totalDrugs = stockService.getTotalStocks();
        int expiredDrugs = stockService.getExpiredDrugs();
        int outOfStockDrugs = stockService.getOutOfStockDrugs();
     // Retrieve analysis data from the service
        
        Map<String, Long> drugCategoryDistribution = stockService.getDrugCategoryDistribution();
        Map<String, Long> topManufacturers = stockService.getTopManufacturers();
        
        double averageStockQuantity = stockService.getAverageStockQuantity();
        int minStockQuantity = stockService.getMinStockQuantity();
        int maxStockQuantity = stockService.getMaxStockQuantity();
        double averagePrice = stockService.getAveragePrice();
        int minPrice = stockService.getMinPrice();
        int maxPrice = stockService.getMaxPrice();

        // Add data to the model
        model.addAttribute("totalDrugs", totalDrugs);
        model.addAttribute("expiredDrugs", expiredDrugs);
        model.addAttribute("outOfStockDrugs", outOfStockDrugs);
        
     // Add analysis data to the model
        model.addAttribute("totalDrugs", totalDrugs);
        model.addAttribute("drugCategoryDistribution", drugCategoryDistribution);
        model.addAttribute("topManufacturers", topManufacturers);
        
        model.addAttribute("averageStockQuantity", averageStockQuantity);
        model.addAttribute("minStockQuantity", minStockQuantity);
        model.addAttribute("maxStockQuantity", maxStockQuantity);
        model.addAttribute("averagePrice", averagePrice);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "/stock/StockDashboard"; // Return the HTML template name
    }

	
	@Autowired
    private  StockRepository repository;
	    @Autowired
    private SupplierRepository supplierRepository; 
	
    @GetMapping("/check")
    public ResponseEntity<String> checkStock(@RequestParam String drugId, @RequestParam int quantity) {
        Stock stock = stockService.findByDrugId(drugId);
    
        if (stock == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drug not found");
        }
    
        if (stock.getQuantity() >= quantity) {
            return ResponseEntity.ok("Stock available");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient stock");
        }
    }
    
    @GetMapping("/stock")
    public String showStockList(Model model) {
        model.addAttribute("stocks", repository.findAll());
        return "stock/stock";
    }
    @PostMapping("/stock")
    public String showstockList(Model model) {
        model.addAttribute("stocks", repository.findAll());
        return "stock/stock";
    }

   
    
    

    
    @GetMapping("/stock/delete/{drugID}")
    public String deleteStock(@PathVariable("drugID") int id) {
        // Assuming the repository has a deleteById method
        repository.deleteById(id);
        return "redirect:/stock";
    }
    
    
    @GetMapping("/stock/edit/{drugID}")
    public String showUpdateStock(@PathVariable("drugID") int id, Model model) {        

        Optional<Stock> stockDTO = repository.findById(id);
        model.addAttribute("stockDTO", stockDTO);
        return "stock/EditStock";
    }    
    
    
    @PostMapping("/stock/edit")
    public String editStock(Stock stock) {
        
        repository.save(stock);
        return "redirect:/stock";
    }
    
    @GetMapping("/stock/view/{drugID}")
    public String showViewStock(@PathVariable("drugID") int id, Model model) {        

        Optional<Stock> stockDTO = repository.findById(id);
        model.addAttribute("stockDTO", stockDTO);
        return "stock/ViewStock";
    }    
    
    
    @PostMapping("/stock/view")
    public String viewStock(Stock stock) {
                
        return "stock/ViewStock";
    }
    
    @GetMapping("/searchDrug")
    public String searchDrug(@RequestParam String keyword, Model model) {
        List<Stock> filteredStocks = stockService.filterStocksByDrugName(keyword);
        model.addAttribute("stocks", filteredStocks);
        return "stock/stock"; // return the name of the view
    }
    @GetMapping("/stock/export")
public void exportToCSV(HttpServletResponse response) throws IOException {
    response.setContentType("text/csv");
    response.setHeader("Content-Disposition", "attachment; filename=pharmacy_stock_data.csv");
    
    List<Stock> stocks = stockService.getAllStocks();
    
    try (PrintWriter writer = response.getWriter()) {
        // Write CSV header
        writer.println("DrugName,DrugCategory,Manufacturer,ManufacturedDate,ExpireDate,Use,SideEffects,Quantity,UnitPrice");
        
        // Write data rows
        for (Stock stock : stocks) {
            writer.println(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,%.2f",
                stock.getDrugName(),
                stock.getDrugCategory(),
                stock.getManufacturer(),
                stock.getManufacturedDate(),
                stock.getExpiredDate(),
                stock.getDrugUse(),
                stock.getSideEffect(),
                stock.getQuantity(),
                stock.getPrice()));
        }
    }
}
@GetMapping("/stock/create")
public String showCreateStockPage(Model model) {
    StockDTO stockDTO = new StockDTO();
    model.addAttribute("stockDTO", stockDTO);
    model.addAttribute("suppliers", supplierRepository.findAll()); // Add this line
    return "stock/CreateStock";
}

@PostMapping("/stock/create")
public String createStock(
        @Valid @ModelAttribute StockDTO stockDTO,
        BindingResult result) {
    
    if (result.hasErrors()) {
        return "stock/CreateStock";
    }
    
    Stock stock = new Stock();
    stock.setDrugName(stockDTO.getDrugName());
        stock.setDrugCategory(stockDTO.getDrugCategory());
        stock.setManufacturer(stockDTO.getManufacturer());
        stock.setManufacturedDate(stockDTO.getManufacturedDate());
        stock.setExpiredDate(stockDTO.getExpiredDate());
        stock.setQuantity(stockDTO.getQuantity());
        stock.setPrice(stockDTO.getPrice());
        stock.setDrugUse(stockDTO.getDrugUse());
        stock.setSideEffect(stockDTO.getSideEffect());

    
    if (stockDTO.getSupplierId() != null) {
        Supplier supplier = supplierRepository.findById(stockDTO.getSupplierId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID"));
        stock.setSupplier(supplier);
    }
    
    repository.save(stock);
    return "redirect:/stock";
}
    
    
}