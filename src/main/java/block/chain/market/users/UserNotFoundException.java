package block.chain.market.users;

public class UserNotFoundException extends RuntimeException{

	public UserNotFoundException(Long id) {
		super("Could not find user " + id);
		// TODO Auto-generated constructor stub
	}

}