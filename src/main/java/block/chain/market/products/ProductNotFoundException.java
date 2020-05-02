package block.chain.market.products;

public class ProductNotFoundException extends RuntimeException{

	public ProductNotFoundException(Long id) {
		super("Could not find product " + id);
		// TODO Auto-generated constructor stub
	}

}
