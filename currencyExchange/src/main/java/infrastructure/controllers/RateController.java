package infrastructure.controllers;

import domain.entities.ExchangeRate;
import infrastructure.common.ConversionHandler;
import infrastructure.entities.Response;
import infrastructure.entities.ResponseBuilder;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import deprecated.RateResponse;
import services.ExchangeRateServiceImpl;

import java.io.IOException;

@RestController
public class RateController {
    @GetMapping("/api/viewRate")
    public Response rateController(@RequestParam String from, @RequestParam String to) throws IOException, ParseException {
        ExchangeRateServiceImpl exchangeRateService = new ExchangeRateServiceImpl();
        ConversionHandler ch = new ConversionHandler();

        ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(ch.toCurrencyCode(from), ch.toCurrencyCode(to));

        ResponseBuilder rb = new ResponseBuilder();

        Response response = rb
                .setFrom(from)
                .setTo(to)
                .setRate(exchangeRate.getToAmount())
                .build();

        return response;
    }
}
