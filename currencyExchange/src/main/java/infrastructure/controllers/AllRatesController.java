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
    @GetMapping("/api/rate")
    public Response allRatesController (@RequestParam String base) throws IOException, ParseException {
        ConversionHandler ch = new ConversionHandler();
        ExchangeRateServiceImpl exchangeRateService = new ExchangeRateServiceImpl(ch);

        ArrayList<ExchangeRate> exchangeRates = null;
        try {
            exchangeRates = exchangeRateService.getAllExchangeRates(ch.toCurrencyCode(base));

        }
        catch (InvalidCurrencyException ice){
            throw ice;
        }

        Map<String, Double> rates = exchangeRates.stream().collect(Collectors.toMap(e -> e.getToCurrency().toString(), ExchangeRate::getToAmount));
        ResponseBuilder rb = new ResponseBuilder();

        Response response = rb
                .setFrom(base)
                .setRates(rates)
                .build();

        return response;

    }

}
