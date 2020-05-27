package block.chain.market.users;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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
import block.chain.market.products.ProductController;
import block.chain.market.products.ProductModelAssembler;
import block.chain.market.products.ProductNotFoundException;
import block.chain.market.products.ProductRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	
	private final UserModelAssembler assembler;

	UserController(UserModelAssembler assembler) {
		this.assembler = assembler;
	  }


	@GetMapping
	CollectionModel<EntityModel<User>> all() {

		List<EntityModel<User>> users = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

	  return new CollectionModel<>(users,
	    linkTo(methodOn(UserController.class).all()).withSelfRel());
	}

	@PostMapping
	ResponseEntity<?> newUser(@RequestBody User newUser){

	  EntityModel<User> entityModel = assembler.toModel(repository.save(newUser));

	  return ResponseEntity
	    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
	    .body(entityModel);
	}

	  // Single item

	  @GetMapping("/{id}")
	  EntityModel<User> one(@PathVariable Long id) {

		  User user  = repository.findById(id)
				  .orElseThrow(() -> new UserNotFoundException(id));
		  
		  return assembler.toModel(user);
	  }

	  @PutMapping("/{id}")
	  ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable Long id){

	    User updateUser = repository.findById(id)
	      .map(user -> {
	        user.setUsername(newUser.getUsername());
	        user.setPassword(newUser.getPassword());
	        return repository.save(user);
	      })
	      .orElseGet(() -> {
	        newUser.setId(id);
	        return repository.save(newUser);
	      });
	    
	    EntityModel<User> entityModel = assembler.toModel(updateUser);
	    
	    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
	    		.body(entityModel);
	  }

	  @DeleteMapping("/{id}")
	  ResponseEntity<?> deleteUser(@PathVariable Long id) {
	    
		  repository.deleteById(id);
		  
		  return ResponseEntity.noContent().build();
	  }
}