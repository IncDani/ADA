package block.chain.market.orders;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import block.chain.market.products.Product;
import block.chain.market.products.ProductModelAssembler;
import block.chain.market.products.ProductNotFoundException;
import block.chain.market.products.ProductRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	private final OrderModelAssembler orderAssembler;
	private final ProductModelAssembler productAssmebler;

	OrderController(OrderModelAssembler orderAssembler, ProductModelAssembler productAssembler) {
		
//		this.orderRepository = orderRepository;
		this.orderAssembler = orderAssembler;
		this.productAssmebler = productAssembler;
		
	}

	@GetMapping
	CollectionModel<EntityModel<Order>> all() {
		
		List<EntityModel<Order>> orders = orderRepository.findAll().stream()
				.map(orderAssembler::toModel)
				.collect(Collectors.toList());

		return new CollectionModel<>(orders,
				linkTo(methodOn(OrderController.class).all()).withSelfRel());
	  }
	
	@GetMapping("/{id}")
	EntityModel<Order> one(@PathVariable Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));
		
		return orderAssembler.toModel(order);
	}
	
	@GetMapping("/{id}/products")
	CollectionModel<EntityModel<Product>> products(@PathVariable Long id){
		
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));
		
		List<EntityModel<Product>> products = productRepository.findAllById(order.getProductsId())
				.stream()
				.map(productAssmebler::toModel)
				.collect(Collectors.toList());
		
//		for(Long productId: order.getProductsId()) {
//			Product product = productRepository.findById(productId)
//					.orElseThrow(() -> new ProductNotFoundException(productId));
//		}
		
		return new CollectionModel<>(products);
		
	}

	@PostMapping
	ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {

		order.setStatus(Status.IN_PROGRESS);
		
		List<Float> priceList = productRepository.findAllById(order.getProductsId())
				.stream()
				.map(Product::getPrice)
				.collect(Collectors.toList());
		
		try {
			float totalValue = 0;
			
			for(int i = 0; i < priceList.size(); i++) {
				totalValue += priceList.get(i)  * order.getProductsQuantity().get(i);
			}
			
			order.setTotalValue(totalValue);
		}catch (Exception e) {
			// TODO: handle exception
			
		}
		
		Order newOrder = orderRepository.save(order);

		return ResponseEntity
				.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
				.body(orderAssembler.toModel(newOrder));
  }
	  
	@DeleteMapping("/{id}/cancel")
	ResponseEntity<RepresentationModel> cancel(@PathVariable Long id) {

		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Status.IN_PROGRESS) {
			order.setStatus(Status.CANCELLED);
			return ResponseEntity.ok(orderAssembler.toModel(orderRepository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
  }
	
	@PutMapping("/{id}/complete")
	ResponseEntity<RepresentationModel> complete(@PathVariable Long id) {

		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Status.IN_PROGRESS) {
			order.setStatus(Status.COMPLETED);
			return ResponseEntity.ok(orderAssembler.toModel(orderRepository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
}
}
