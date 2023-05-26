package infrastructure.controllers;
import domain.entities.ExchangeRate;
import domain.exceptions.InvalidCurrencyException;
import infrastructure.common.ConversionHandler;
import infrastructure.entities.Response;
import infrastructure.entities.ResponseBuilder;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import services.ExchangeRateServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AllRatesController extends BaseController{
    //Extending the Base Controller makes it possible to catch the exceptions created
    //So that there's no necessity for keeping on throwing exception on every REST controller
    @GetMapping("/api/rate") //Defining the endpoint for this operation
    public Response allRatesController (@RequestParam String base) throws IOException, ParseException {
        //This controller is responsible for building the response that shows all the conversion rates related to one specific based currency
        ExchangeRateServiceImpl exchangeRateService = new ExchangeRateServiceImpl(ch, cache);

        ArrayList<ExchangeRate> exchangeRates = null;
        exchangeRates = exchangeRateService.getAllExchangeRates(ch.toCurrencyCode(base)); //Accesses the logic from the services, and passes the user input
        Map<String, Double> rates = exchangeRates.stream().collect(Collectors.toMap(e -> e.getToCurrency().toString(), ExchangeRate::getToAmount));
        //Converting the ArrayList to a stream so that we can collect, then using a map, because it makes it possible that there's a key for every currency
        //Using lambda expressions to iterate the map

        //Builder design pattern to create the response
        return new ResponseBuilder()
                .setFrom(base)
                .setRates(rates)
                .build();
    }
}
