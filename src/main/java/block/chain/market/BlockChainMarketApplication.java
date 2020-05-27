package block.chain.market;

import org.freesoftware.admin.soap.webservice.SOAPWebServiceProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//
//import block.chain.market.config.ServerConfiguration;

import block.chain.market.web.WebDataProvider;

@SpringBootApplication
//@ConfigurationPropertiesScan("block.chain.market.config")
//@EnableConfigurationProperties(ServerConfiguration.class)
public class BlockChainMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockChainMarketApplication.class, args);
	
		String url = "http://localhost:8082/admin";
		WebDataProvider repo = new WebDataProvider();
		SOAPWebServiceProvider webServer = new SOAPWebServiceProvider();
		webServer.begin(repo, url);
	}

}
