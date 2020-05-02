package block.chain.market.products;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "products")
public class Product {
	
	private @Id @GeneratedValue Long id;
	private String name;
	private String category;
	private int stock;
	private float price;
	private String country;
	
	public Product() {}

	public Product(String name, String category, int stock, float price, String country) {
		super();
		this.name = name;
		this.category = category;
		this.stock = stock;
		this.price = price;
		this.country = country;
	}
}
