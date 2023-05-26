import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })

@ComponentScan({"infrastructure.controllers", "infrastructure.error"})

public class CurrencyExchangeApplication {

	public static void main(String[] args) throws IOException, ParseException {
		SpringApplication.run(CurrencyExchangeApplication.class, args);
	}
}


