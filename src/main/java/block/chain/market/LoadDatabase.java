package block.chain.market;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import block.chain.market.orders.Order;
import block.chain.market.orders.OrderRepository;
import block.chain.market.orders.Status;
import block.chain.market.products.Product;
import block.chain.market.products.ProductRepository;

@Configuration
@Slf4j
class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(ProductRepository productRepository, OrderRepository orderRepository) {
    return args -> {
      log.info("Preloading " + productRepository.save(new Product("Chocolate", "Food", 600, 5, "Romania")));
      log.info("Preloading " + productRepository.save(new Product("Monitor", "Electronics", 250, 1000, "Germany")));
      
      log.info("Preloading " + orderRepository.save(new Order("Chocolate", Status.COMPLETED)));
      log.info("Preloading " + orderRepository.save(new Order("Monitor", Status.IN_PROGRESS)));
    };
    
	
    
  }
}
