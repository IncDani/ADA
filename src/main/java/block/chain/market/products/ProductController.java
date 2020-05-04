package block.chain.market.products;

//import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
//import org.springframework.hateoas.CollectionModel;


@RestController
@RequestMapping("/products")
public class ProductController {
	private final ProductRepository repository;
	private final ProductModelAssembler assembler;

	ProductController(ProductRepository repository, ProductModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	  }

	  // Aggregate root

	@GetMapping
	CollectionModel<EntityModel<Product>> all() {

		List<EntityModel<Product>> products = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

	  return new CollectionModel<>(products,
	    linkTo(methodOn(ProductController.class).all()).withSelfRel());
	}

	@PostMapping
	ResponseEntity<?> newProduct(@RequestBody Product newProduct){

	  EntityModel<Product> entityModel = assembler.toModel(repository.save(newProduct));

	  return ResponseEntity
	    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
	    .body(entityModel);
	}

	  // Single item

	  @GetMapping("/{id}")
	  EntityModel<Product> one(@PathVariable Long id) {

		  Product product  = repository.findById(id)
				  .orElseThrow(() -> new ProductNotFoundException(id));
		  
		  return assembler.toModel(product);
	  }

	  @PutMapping("/{id}")
	  ResponseEntity<?> replaceProduct(@RequestBody Product newProduct, @PathVariable Long id){

	    Product updateProduct = repository.findById(id)
	      .map(product -> {
	        product.setName(newProduct.getName());
	        product.setPrice(newProduct.getPrice());
	        return repository.save(product);
	      })
	      .orElseGet(() -> {
	        newProduct.setId(id);
	        return repository.save(newProduct);
	      });
	    
	    EntityModel<Product> entityModel = assembler.toModel(updateProduct);
	    
	    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
	    		.body(entityModel);
	  }

	  @DeleteMapping("/{id}")
	  ResponseEntity<?> deleteProduct(@PathVariable Long id) {
	    
		  repository.deleteById(id);
		  
		  return ResponseEntity.noContent().build();
	  }
}
