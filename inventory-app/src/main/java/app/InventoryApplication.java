package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import service.InventoryService;

@SpringBootApplication
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @Bean
    public InventoryService inventoryService() {
        String dataFile = System.getenv().getOrDefault(
            "INVENTORY_FILE",
            "data/inventory.csv"
        );
        return new InventoryService(dataFile);
    }
}
