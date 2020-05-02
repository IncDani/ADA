package block.chain.market.orders;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private final OrderRepository orderRepository;
	private final OrderModelAssembler assembler;

	OrderController(OrderRepository orderRepository,
			OrderModelAssembler assembler) {
		
		this.orderRepository = orderRepository;
		this.assembler = assembler;
		
	}

	@GetMapping
	CollectionModel<EntityModel<Order>> all() {
		
		List<EntityModel<Order>> orders = orderRepository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

		return new CollectionModel<>(orders,
				linkTo(methodOn(OrderController.class).all()).withSelfRel());
	  }
	@GetMapping("/{id}")
	EntityModel<Order> one(@PathVariable Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));
		
		return assembler.toModel(order);
  }

	@PostMapping
	ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {

		order.setStatus(Status.IN_PROGRESS);
		Order newOrder = orderRepository.save(order);

		return ResponseEntity
				.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
				.body(assembler.toModel(newOrder));
  }
	  
	@DeleteMapping("/{id}/cancel")
	ResponseEntity<RepresentationModel> cancel(@PathVariable Long id) {

		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Status.IN_PROGRESS) {
			order.setStatus(Status.CANCELLED);
			return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
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
			return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)));
		}

		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
}
}
