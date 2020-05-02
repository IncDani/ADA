package block.chain.market.orders;

public class OrderNotFoundException extends RuntimeException{

	public OrderNotFoundException(Long id) {
		super("Could not find order " + id);
		// TODO Auto-generated constructor stub
	}
}
