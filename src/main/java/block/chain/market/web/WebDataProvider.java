package block.chain.market.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.freesoftware.admin.soap.webservice.data.WebServiceDataProvider;
import org.freesoftware.admin.soap.webservice.product.Product;
import org.springframework.stereotype.Controller;

import block.chain.market.products.ProductRepository;

@Controller
public class WebDataProvider implements WebServiceDataProvider {

	private ProductRepository productRepository;
	private List<WebUser> users = new ArrayList<>();
	
	public WebDataProvider()
	{
		productRepository = ApplicationContextHolder.getContext().getBean(ProductRepository.class);
	}
	
	@Override
	public void addProduct(String arg0, String arg1, int arg2, float arg3, String arg4) {
		block.chain.market.products.Product newProduct = new block.chain.market.products.Product();
		newProduct.setName(arg0);
		newProduct.setCategory(arg1);
		newProduct.setStock(arg2);
		newProduct.setPrice(arg3);
		newProduct.setCountry(arg4);
		productRepository.save(newProduct);
	}

	@Override
	public void addQuantity(long arg0, int arg1) {
		Optional<block.chain.market.products.Product> serverProduct = productRepository.findById(arg0);
		if(serverProduct.isPresent())
		{
			serverProduct.get().setStock(serverProduct.get().getStock()+arg1);
			productRepository.save(serverProduct.get());
		}
	}
	
	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		if(productRepository==null)
		{
			System.out.println("WEB SERVER PRODUCT REPOSITORY IS NULL!");
			return products;
		}
		List<block.chain.market.products.Product> serverProducts = productRepository.findAll();
		for(block.chain.market.products.Product serverProduct : serverProducts)
		{
			String country = "Romania"; //TODO: replace this with serverProduct.getCountry();
			Product product = new Product(serverProduct.getId(), serverProduct.getName(), serverProduct.getCategory(), serverProduct.getStock(), serverProduct.getPrice(), country);
			products.add(product);
		}
		return products;
	}

	@Override
	public boolean loginUser(String arg0, String arg1) {
		for(WebUser user : users)
		{
			if(user.getUsername().equals(arg0))
			{
				if(user.passwordMatch(arg1)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean registerUser(String arg0, String arg1) {
		for(WebUser user :users)
		{
			if(user.getUsername().equals(arg0))
			{
				return false;
			}
		}
		WebUser newUser = new WebUser(arg0, arg1);
		users.add(newUser);
		return true;
	}

	@Override
	public void removeProduct(long arg0) {
		productRepository.deleteById(arg0);
	}

	@Override
	public void removeQuantity(long arg0, int arg1) {
		Optional<block.chain.market.products.Product> serverProduct = productRepository.findById(arg0);
		if(serverProduct.isPresent())
		{
			if(serverProduct.get().getStock()<arg1)
			{
				return;
			}
			serverProduct.get().setStock(serverProduct.get().getStock()-arg1);
			productRepository.save(serverProduct.get());
		}
	}
}
