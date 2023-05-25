package services;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ExchangeRateServiceImpl implements IExchangeRateService{
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
            Double result = (Double) ((Map<?, ?>) jsonObject).get("result");
            exchangeRate = new ExchangeRate(fromCurrency, toCurrency, null, result);
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

            Number exchangableAmount = (Number) ((JSONObject) ((Map<?, ?>) jsonObject).get("query")).get("amount");
            Double result = (Double) ((Map<?, ?>) jsonObject).get("result");


            exchangeRate = new ExchangeRate(fromCurrency, toCurrency, amount, result);
        } catch (Exception e){
            e.printStackTrace();
        }

        return exchangeRate;
    }

    @Override
    public ArrayList<ExchangeRate> getMultipleExchangeRates(CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        return null;
    }

    @Override
    public ArrayList<ExchangeRate> getAllExchangeRates(CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        return null;
    }
}
