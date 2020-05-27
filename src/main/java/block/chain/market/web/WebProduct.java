package block.chain.market.web;

import org.freesoftware.admin.soap.webservice.product.Product;

public class WebProduct extends Product{

	private static final long serialVersionUID = -4155258440506021638L;

	public WebProduct(long id, String name, String cat, int stock, float price, String country) {
		super(id, name, cat, stock, price, country);
	}

}
