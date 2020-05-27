package block.chain.market.products;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Data
@Entity
@Table(name = "products")
public class Product {
	
	private @Id @GeneratedValue Long id;
	private String name;
	private String category;
	private int stock;
	private float price;
	private String country;	// try to make String
	
	public Product() {}

	public Product(String name, String category, int stock, float price) {
		super();
		this.name = name;
		this.category = category;
		this.stock = stock;
		this.price = price;
	}
}
