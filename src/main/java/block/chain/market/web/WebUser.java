package block.chain.market.web;

import org.freesoftware.admin.soap.webservice.user.User;

public class WebUser extends User{

	private static final long serialVersionUID = -5083767089901006316L;

	public WebUser(String username, String password) {
		super(username, password);
	}

}
