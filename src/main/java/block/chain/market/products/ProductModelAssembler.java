package block.chain.market.products;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

	@Override
	public EntityModel<Product> toModel(Product product) {
		// TODO Auto-generated method stub
		return new EntityModel<>(product,
				linkTo(methodOn(ProductController.class).one(product.getId())).withSelfRel(),
				linkTo(methodOn(ProductController.class).all()).withRel("products"));
	}

}
