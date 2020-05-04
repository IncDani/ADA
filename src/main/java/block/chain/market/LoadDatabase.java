package block.chain.market;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
    
    	Product chocolate = new Product("Chocolate", "Food", 600, 5, 1);
  	  	Product monitor = new Product("Monitor", "Electronics", 250, 1000, 1);
  	  	Product t_shirt = new Product("T-Shirt", "Clothes", 1000, 10, 1);
  	  	Product cheese = new Product("Cheese", "Food", 450, 6, 1);
    	
  	  	log.info("Preloading " + productRepository.save(chocolate));
  	  	log.info("Preloading " + productRepository.save(monitor));
  	  	log.info("Preloading " + productRepository.save(t_shirt));
  	  	log.info("Preloading " + productRepository.save(cheese));
      
  	  	Order order1 = new Order(new HashSet<>(Arrays.asList(chocolate.getId(), t_shirt.getId())),
  	  			new ArrayList<>(Arrays.asList(3, 6)), 75, Status.COMPLETED);
  	  	Order order2 = new Order(new HashSet<>(Arrays.asList(monitor.getId(), t_shirt.getId())),
  	  		new ArrayList<>(Arrays.asList(2, 1)), 2010, Status.IN_PROGRESS);
  	  	Order order3 = new Order(new HashSet<>(Arrays.asList(cheese.getId())),
  	  		new ArrayList<>(Arrays.asList(4)), 24, Status.CANCELLED);
      
  	  	log.info("Preloading " + orderRepository.save(order1));
  	  	log.info("Preloading " + orderRepository.save(order2));
  	  	log.info("Preloading " + orderRepository.save(order3));
    };
    
	
    
  }
}
