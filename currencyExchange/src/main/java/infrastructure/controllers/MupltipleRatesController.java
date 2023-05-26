package infrastructure.controllers;

import domain.entities.ExchangeRate;
import domain.exceptions.InvalidCurrencyException;
import infrastructure.error.ErrorResponse;
import infrastructure.common.ConversionHandler;
import infrastructure.entities.Response;
import infrastructure.entities.ResponseBuilder;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.ExchangeRateServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MupltipleRatesController extends BaseController {
    @GetMapping("/api/latest")
    public Response multipleRatesController(@RequestParam String from, @RequestParam String to) throws IOException, ParseException {
        ExchangeRateServiceImpl exchangeRateService = new ExchangeRateServiceImpl(ch, cache);

        ArrayList<ExchangeRate> exchangeRates = null;
        //Spliting the desired "to currencies" with commas, so that it can be appended and using a lambda expression to
        //iterate the map that contains the Currency Code and the rate when met with the base currency
        exchangeRates =
                exchangeRateService.getMultipleExchangeRates(ch.toCurrencyCode(from), Arrays
                        .stream(to.split(","))
                        .map(c -> ch.toCurrencyCode(c))
                        .collect(Collectors.toCollection(ArrayList::new)));

        Map<String, Double> rates = exchangeRates.stream().collect(Collectors.toMap(e -> e.getToCurrency().toString(), ExchangeRate::getToAmount, (f, s) -> f));
        //When there's multiple request for the same Currency, this (f, s) -> f) makes sure that only the first one of the same currency is displayed

        return new ResponseBuilder()
                .setFrom(from)
                .setRates(rates)
                .build();
    }
}
