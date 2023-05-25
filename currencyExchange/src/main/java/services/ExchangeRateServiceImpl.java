package services;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;
import infrastructure.common.ConversionHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class ExchangeRateServiceImpl implements IExchangeRateService{

    private ConversionHandler ch;

    public ExchangeRateServiceImpl(ConversionHandler ch) {
        this.ch = ch;
    }

    @Override
    public ExchangeRate getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency) { // View Rate from Currency A to currency B
        String urlStr = "https://api.exchangerate.host/convert?from=" + fromCurrency + "&to=" + toCurrency;

        ExchangeRate exchangeRate = null;

        try {
            // Create http connection
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Parse JSON response
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

            // Create ExchangeRate
            Number result = (Number) ((Map<?, ?>) jsonObject).get("result");
            exchangeRate = new ExchangeRate(fromCurrency, toCurrency, null, result.doubleValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exchangeRate;
    }

    @Override
    public ExchangeRate exchangeCurrency(CurrencyCode fromCurrency, CurrencyCode toCurrency, Double amount) {
        String urlStr = "https://api.exchangerate.host/convert?from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount;

        ExchangeRate exchangeRate = null;

        try{
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

            Number result = (Number) ((Map<?, ?>) jsonObject).get("result");


            exchangeRate = new ExchangeRate(fromCurrency, toCurrency, amount, result.doubleValue());
        } catch (Exception e){
            e.printStackTrace();
        }

        return exchangeRate;
    }

    @Override
    public ArrayList<ExchangeRate> getMultipleExchangeRates(CurrencyCode fromCurrency, ArrayList<CurrencyCode> toCurrency) {
        String urlStr = "https://api.exchangerate.host/latest?base=" + fromCurrency + "&symbols=" + String.join(",", toCurrency.stream().map(CurrencyCode::toString).toList());

        ArrayList<ExchangeRate> exchangeRates = null;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

            Map<String, Number> rates = (Map<String, Number>) ((Map<?, ?>) jsonObject).get("rates");

            exchangeRates = toCurrency
                    .stream()
                    .map(r -> new ExchangeRate(fromCurrency, r, 1.0, rates.get(r.toString()).doubleValue()))
                    .collect(Collectors.toCollection(ArrayList::new));


        } catch (Exception e){
            e.printStackTrace();
        }

        return exchangeRates;
    }

    @Override
    public ArrayList<ExchangeRate> getAllExchangeRates(CurrencyCode fromCurrency) {
        String urlStr = "https://api.exchangerate.host/viewrate?base=" + fromCurrency;
        ArrayList<ExchangeRate> exchangeRates = null;

        try{
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

            Map<String, Number> rates = (Map<String, Number>) ((Map<?, ?>) jsonObject).get("rates");
            System.out.println(rates);

            exchangeRates = rates
                    .keySet()
                    .stream()
                    .map(r -> new ExchangeRate(fromCurrency, ch.toCurrencyCode(r), 1.0, rates.get(r).doubleValue()))
                    .collect(Collectors.toCollection(ArrayList::new));

            System.out.println(exchangeRates);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return exchangeRates;
        }

}
