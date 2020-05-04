package block.chain.market.products;

public class InsufficientStockException extends RuntimeException {

	public InsufficientStockException(Integer requested, Integer available) {
		super("Insufficient stock available. Available quantity " + available
				+ "less than requested quantity " + requested + ".");
	}
}
