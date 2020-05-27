package block.chain.market.users;

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
public class User {
	
	private @Id @GeneratedValue Long id;
	private String username;
	private String password;

	public User() {}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
}
