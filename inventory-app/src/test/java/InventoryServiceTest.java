import model.Product;
import service.InventoryService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    @TempDir
    Path tempDir;

    private InventoryService service;

    @BeforeEach
    void setUp() {
        service = new InventoryService(
            tempDir.resolve("inventory.csv").toString()
        );
    }

    @Test
    void shouldAddProduct() {
        Product product = service.addProduct(
            "Laptop", "Electronics", 50000, 10
        );

        assertNotNull(product);
        assertEquals("Laptop", product.getName());
        assertEquals(10, product.getQuantity());
    }

    @Test
    void shouldRemoveProduct() {
        Product product = service.addProduct(
            "Mouse", "Electronics", 500, 5
        );

        assertTrue(service.removeProduct(product.getId()));
        assertTrue(service.getAllProducts().isEmpty());
    }

    @Test
    void shouldSearchProductByName() {
        service.addProduct("Laptop", "Electronics", 50000, 10);
        service.addProduct("Mouse", "Electronics", 500, 5);

        List<Product> results = service.searchByName("Laptop");

        assertEquals(1, results.size());
        assertEquals("Laptop", results.get(0).getName());
    }

    @Test
    void shouldCalculateTotalInventoryValue() {
        service.addProduct("Laptop", "Electronics", 50000, 2);
        service.addProduct("Mouse", "Electronics", 500, 4);

        assertEquals(
            102000.0,
            service.getTotalInventoryValue(),
            0.01
        );
    }

    @Test
    void shouldIdentifyLowStockProducts() {
        service.addProduct("Keyboard", "Electronics", 1000, 2);
        service.addProduct("Monitor", "Electronics", 10000, 20);

        assertEquals(1, service.getLowStockProducts().size());
    }

    @Test
    void shouldFindProductById() {
        Product product = service.addProduct(
            "Keyboard", "Electronics", 1000, 10
        );

        assertTrue(service.findById(product.getId()).isPresent());
        assertEquals(
            "Keyboard",
            service.findById(product.getId()).get().getName()
        );
    }

    @Test
    void shouldFilterProductsByCategory() {
        service.addProduct("Laptop", "Electronics", 50000, 5);
        service.addProduct("Chair", "Furniture", 3000, 10);

        List<Product> results =
            service.filterByCategory("Electronics");

        assertEquals(1, results.size());
        assertEquals("Laptop", results.get(0).getName());
    }

    @Test
    void shouldUpdateLowStockThreshold() {
        service.setLowStockThreshold(10);

        service.addProduct("Keyboard", "Electronics", 1000, 3);
        service.addProduct("Monitor", "Electronics", 10000, 20);

        assertEquals(1, service.getLowStockProducts().size());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonexistentProduct() {
        assertFalse(service.removeProduct("P999"));
    }

    @Test
    void shouldSearchProductCaseInsensitively() {
        service.addProduct("Laptop", "Electronics", 50000, 10);

        List<Product> results = service.searchByName("laptop");

        assertEquals(1, results.size());
    }

    @Test
    void shouldReturnEmptyListWhenProductDoesNotExist() {
        List<Product> results = service.searchByName("Phone");

        assertTrue(results.isEmpty());
    }
}