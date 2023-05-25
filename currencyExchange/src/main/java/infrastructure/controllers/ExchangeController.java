package infrastructure.controllers;

import domain.entities.ExchangeRate;
import domain.exceptions.InvalidCurrencyException;
import infrastructure.common.ConversionHandler;
import infrastructure.entities.Response;
import infrastructure.entities.ResponseBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import deprecated.CurrencyConverterResponse;
import services.ExchangeRateServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

@RestController
public class ExchangeController extends BaseController {
    @GetMapping("/api/convert")
        public Response exchangeController(@RequestParam String from, @RequestParam String to, @RequestParam Double amount) throws IOException, ParseException {
        ConversionHandler ch = new ConversionHandler();

        ExchangeRateServiceImpl exchangeRateService = new ExchangeRateServiceImpl(ch);
        ExchangeRate exchangeRate = null;

            try {
                exchangeRate = exchangeRateService.exchangeCurrency(ch.toCurrencyCode(from), ch.toCurrencyCode(to), amount);
            }
            catch (InvalidCurrencyException ice){
                throw ice;
            }
            ResponseBuilder rb = new ResponseBuilder();

            Response response = rb
                    .setFrom(from)
                    .setTo(to)
                    .setAmount(amount)
                    .setResult(exchangeRate.getToAmount())
                    .build();

            return response;
    }
}
