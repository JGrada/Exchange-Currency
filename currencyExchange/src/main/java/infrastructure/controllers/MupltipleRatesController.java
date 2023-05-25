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
        ConversionHandler ch = new ConversionHandler();
        ExchangeRateServiceImpl exchangeRateService = new ExchangeRateServiceImpl(ch);

        ArrayList<ExchangeRate> exchangeRates = null;

        try {
            exchangeRates =
                    exchangeRateService.getMultipleExchangeRates(ch.toCurrencyCode(from), Arrays
                            .stream(to.split(","))
                            .map(c -> ch.toCurrencyCode(c))
                            .collect(Collectors.toCollection(ArrayList::new)));
        } catch (InvalidCurrencyException ice) {
            throw ice;
        }



        ResponseBuilder rb = new ResponseBuilder();
        Map<String, Double> rates = exchangeRates.stream().collect(Collectors.toMap(e -> e.getToCurrency().toString(), ExchangeRate::getToAmount, (f, s) -> f));


        Response response = rb
                .setFrom(from)
                .setRates(rates)
                .build();

        return response;
    }
}
