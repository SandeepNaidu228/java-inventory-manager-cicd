package app.api;

import model.Product;
import service.InventoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return inventoryService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        return inventoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request) {
        if (request.name() == null || request.name().isBlank()
                || request.category() == null || request.category().isBlank()
                || request.price() <= 0 || request.quantity() < 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid product details"));
        }

        Product product = inventoryService.addProduct(
                request.name(),
                request.category(),
                request.price(),
                request.quantity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        if (inventoryService.removeProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/inventory/value")
    public Map<String, Double> getInventoryValue() {
        return Map.of("totalValue", inventoryService.getTotalInventoryValue());
    }

    @GetMapping("/inventory/low-stock")
    public List<Product> getLowStockProducts() {
        return inventoryService.getLowStockProducts();
    }

    @GetMapping("/products/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return inventoryService.searchByName(name);
    }

    public record ProductRequest(
            String name,
            String category,
            double price,
            int quantity
    ) {}
}
