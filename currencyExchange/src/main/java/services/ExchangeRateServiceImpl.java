package services;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;
import domain.exceptions.InvalidValueException;
import infrastructure.common.ConversionHandler;
import infrastructure.persistence.cache.ICache;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.relational.core.sql.In;

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
    private ICache cache;

    public ExchangeRateServiceImpl(ConversionHandler ch, ICache cache) {
        this.ch = ch;
        this.cache = cache;
    }

    @Override
    public ExchangeRate getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency) { // View Rate from Currency A to currency B
        String urlStr = "https://api.exchangerate.host/convert?from=" + fromCurrency + "&to=" + toCurrency;

        ExchangeRate exchangeRate = null;

        if (cache.hasCachedExchangeRate(fromCurrency, toCurrency, 1.0)) {
            exchangeRate = cache.getCachedExchangeRate(fromCurrency, toCurrency, 1.0);
            System.out.println("from cache");
        } else {
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
                exchangeRate = new ExchangeRate(fromCurrency, toCurrency, 1.0, result.doubleValue());
                cache.cacheExchangeRate(fromCurrency, toCurrency, 1.0, result.doubleValue());
                System.out.println("from http");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return exchangeRate;
    }

    @Override
    public ExchangeRate exchangeCurrency(CurrencyCode fromCurrency, CurrencyCode toCurrency, Double amount) throws InvalidValueException {
        String urlStr = "https://api.exchangerate.host/convert?from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount;
        if(amount <= 0){
            throw new InvalidValueException("Invalid value");
        }
        ExchangeRate exchangeRate = null;

        if (cache.hasCachedExchangeRate(fromCurrency, toCurrency, amount)) {
            exchangeRate = cache.getCachedExchangeRate(fromCurrency, toCurrency, amount);
            System.out.println("from cache");
        } else {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

                Number result = (Number) ((Map<?, ?>) jsonObject).get("result");

                exchangeRate = new ExchangeRate(fromCurrency, toCurrency, amount, result.doubleValue());
                cache.cacheExchangeRate(fromCurrency, toCurrency, amount, result.doubleValue());
                System.out.println("from http");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return exchangeRate;
    }

    @Override
    public ArrayList<ExchangeRate> getMultipleExchangeRates(CurrencyCode fromCurrency, ArrayList<CurrencyCode> toCurrency) {
        String urlStr = "https://api.exchangerate.host/latest?base=" + fromCurrency + "&symbols=" + String.join(",", toCurrency.stream().map(CurrencyCode::toString).toList());

        ArrayList<ExchangeRate> exchangeRates = new ArrayList<ExchangeRate>();
        ArrayList<CurrencyCode> currencyCodesToReq = new ArrayList<CurrencyCode>();

        for(CurrencyCode cc : toCurrency){
            if(cache.hasCachedExchangeRate(fromCurrency, cc, 1.0)){
                exchangeRates.add(cache.getCachedExchangeRate(fromCurrency, cc, 1.0));
                System.out.println(cc.toString() + " was cached");
            } else {
                currencyCodesToReq.add(cc);
                System.out.println(cc.toString() + " will be requested");
            }
        }

        if (!currencyCodesToReq.isEmpty()) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

                Map<String, Number> rates = (Map<String, Number>) ((Map<?, ?>) jsonObject).get("rates");

                ArrayList<ExchangeRate> uncachedExchangeRates = currencyCodesToReq
                        .stream()
                        .map(r -> new ExchangeRate(fromCurrency, r, 1.0, rates.get(r.toString()).doubleValue()))
                        .collect(Collectors.toCollection(ArrayList::new));

                uncachedExchangeRates.forEach(er -> cache.cacheExchangeRate(er.getFromCurrency(), er.getToCurrency(), 1.0, er.getToAmount()));

                exchangeRates.addAll(uncachedExchangeRates);

            } catch (Exception e){
                e.printStackTrace();
            }
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
            exchangeRates.forEach(er -> cache.cacheExchangeRate(er.getFromCurrency(), er.getToCurrency(), 1.0, er.getToAmount()));
            System.out.println(exchangeRates.toString() + "was cached");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return exchangeRates;
        }

}
