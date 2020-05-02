package block.chain.market.orders;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {
	private @Id @GeneratedValue Long id;
	
	private String orderedProduct;
	private Status status;
	
	Order() {}
	
	public Order(String orderProduct, Status status){
		this.orderedProduct = orderProduct;
		this.status = status;
	}
}
