package infrastructure.controllers;

import domain.entities.ExchangeRate;
import domain.exceptions.InvalidCurrencyException;
import infrastructure.common.ConversionHandler;
import infrastructure.entities.Response;
import infrastructure.entities.ResponseBuilder;
import infrastructure.error.ErrorResponse;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import deprecated.RateResponse;
import services.ExchangeRateServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class RateController extends BaseController {
    @GetMapping("/api/viewRate")
    public Response rateController(@RequestParam String from, @RequestParam String to) throws IOException, ParseException {
        ConversionHandler ch = new ConversionHandler();

        ExchangeRateServiceImpl exchangeRateService = new ExchangeRateServiceImpl(ch);
        ExchangeRate exchangeRate = null;

        try {
            exchangeRate = exchangeRateService.getExchangeRate(ch.toCurrencyCode(from), ch.toCurrencyCode(to));
        } catch (InvalidCurrencyException ice) {
            throw ice;
        }

        ResponseBuilder rb = new ResponseBuilder();

        Response response = rb
                .setFrom(from)
                .setTo(to)
                .setRate(exchangeRate.getToAmount())
                .build();

        return response;
    }

}
