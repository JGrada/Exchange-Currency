import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan({"infrastructure.controllers", "infrastructure/error"})

public class CurrencyExchangeApplication {

	public static <JsonElement, JsonObject> void main(String[] args) throws IOException, ParseException {
		SpringApplication.run(CurrencyExchangeApplication.class, args);
	}
}


