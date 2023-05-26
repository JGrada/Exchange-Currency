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

    private final ConversionHandler ch;
    private final ICache cache;

    public ExchangeRateServiceImpl(ConversionHandler ch, ICache cache) {
        this.ch = ch;
        this.cache = cache;
    }
    //Decided to use the documentation provided by the API to access the data, I'm aware that there's other options
    //Like WebClient but decided to go with the example from the source API documentation
    @Override
    public ExchangeRate getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency) { // View Rate from Currency A to currency B

        String urlStr = "https://api.exchangerate.host/convert?from=" + fromCurrency + "&to=" + toCurrency; //Appending the link with the parameters received

        ExchangeRate exchangeRate = null;
        //Verifying if the value is already on cache
        if (cache.hasCachedExchangeRate(fromCurrency, toCurrency, 1.0)) {
            exchangeRate = cache.getCachedExchangeRate(fromCurrency, toCurrency, 1.0);
            System.out.println("from cache");
        } else { //If the value is not on cache, then HTTP request is made
            try {
                // Create http connection
                URL url = new URL(urlStr);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                // Parse JSON response
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

                // Create ExchangeRate
                //Had to use Number for specific problem when converting from EUR to EUR, because the value would be "1" which makes the conversion not working
                Number result = (Number) ((Map<?, ?>) jsonObject).get("result");
                //Creating the new Exchange Rate with the received rate, setting the amount to 1, so that we get the exact rate
                exchangeRate = new ExchangeRate(fromCurrency, toCurrency, 1.0, result.doubleValue());
                //Adding the value to the cache
                cache.cacheExchangeRate(fromCurrency, toCurrency, 1.0, result.doubleValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return exchangeRate;
    }

    @Override
    //Converting specific amount from Currency A to Currency B, ex : 10 EUR to USD
    public ExchangeRate exchangeCurrency(CurrencyCode fromCurrency, CurrencyCode toCurrency, Double amount) throws InvalidValueException {
        String urlStr = "https://api.exchangerate.host/convert?from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount; //Appending the link
        if(amount <= 0){
            throw new InvalidValueException("Invalid value"); //If the amount to be converted is 0 or negative, Exception is thrown
        }
        ExchangeRate exchangeRate = null;

        //Verifying if result is already in cache
        if (cache.hasCachedExchangeRate(fromCurrency, toCurrency, amount)) {
            exchangeRate = cache.getCachedExchangeRate(fromCurrency, toCurrency, amount);
        } else {
            try {
                //Creating connection
                URL url = new URL(urlStr);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                //Parsing response
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

                Number result = (Number) ((Map<?, ?>) jsonObject).get("result"); //Getting the result

                exchangeRate = new ExchangeRate(fromCurrency, toCurrency, amount, result.doubleValue()); //Creating the new Exchange Rate
                cache.cacheExchangeRate(fromCurrency, toCurrency, amount, result.doubleValue()); //Addind result to cache
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return exchangeRate;
    }

    @Override
    //Getting rate from Currency A to Currency B,C,D. Ex: EUR to USD,CAD,RON
    public ArrayList<ExchangeRate> getMultipleExchangeRates(CurrencyCode fromCurrency, ArrayList<CurrencyCode> toCurrency) {
        //Appending the link and separating the "To currency" from the ArrayList with commas
        String urlStr = "https://api.exchangerate.host/latest?base=" + fromCurrency + "&symbols=" + String.join(",", toCurrency.stream().map(CurrencyCode::toString).toList());

        ArrayList<ExchangeRate> exchangeRates = new ArrayList<ExchangeRate>();
        //This was necessary because some values might already be on cache, so decision was verifying which ones weren't, so that they can be requested from HTTP
        ArrayList<CurrencyCode> currencyCodesToReq = new ArrayList<CurrencyCode>();

        for(CurrencyCode cc : toCurrency){
            if(cache.hasCachedExchangeRate(fromCurrency, cc, 1.0)){
                //If value is already on cache, value is added to "final" arraylist
                exchangeRates.add(cache.getCachedExchangeRate(fromCurrency, cc, 1.0));
            } else {
                //If value is NOT on cache, it's added to the ones that are needed to be requested
                currencyCodesToReq.add(cc);
            }
        }
        //If the array of Currencies to be requested is not empty, HTTP request will be made
        //Only for the ones necessary
        if (!currencyCodesToReq.isEmpty()) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection request = (HttpURLConnection) url.openConnection();
                request.connect();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

                Map<String, Number> rates = (Map<String, Number>) ((Map<?, ?>) jsonObject).get("rates");
                //Creating a new ArrayList to store the values, streaming them to iterate them, and creating a Map
                //Followed by lambda operation to add the Exchange Rate to the new ArrayList
                ArrayList<ExchangeRate> uncachedExchangeRates = currencyCodesToReq
                        .stream()
                        .map(r -> new ExchangeRate(fromCurrency, r, 1.0, rates.get(r.toString()).doubleValue()))
                        .collect(Collectors.toCollection(ArrayList::new));
                //Adding the values requested to the cache
                uncachedExchangeRates.forEach(er -> cache.cacheExchangeRate(er.getFromCurrency(), er.getToCurrency(), 1.0, er.getToAmount()));
                //Adding the values requested to the arraylist created above, on line 111, either filling it from the beggining or
                //Just adding the ones that were necessary
                exchangeRates.addAll(uncachedExchangeRates);

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return exchangeRates;
    }

    @Override
    public ArrayList<ExchangeRate> getAllExchangeRates(CurrencyCode fromCurrency) { //Getting all the exchange rates for one single Currency
        String urlStr = "https://api.exchangerate.host/viewrate?base=" + fromCurrency;
        ArrayList<ExchangeRate> exchangeRates = null;

        //Here, given the syntax of the API, it makes it impossible to look for value in cache, this is due
        //To the fact that the link="https://api.exchangerate.host/viewrate?base=" can not be appended with the "Currency to",
        // which makes it impossible to check what values are already in cache

        try{

            //Creating the connection
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            //Parsing the response
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));

            Map<String, Number> rates = (Map<String, Number>) ((Map<?, ?>) jsonObject).get("rates");

            //Setting the response
            exchangeRates = rates
                    .keySet()
                    .stream()
                    .map(r -> new ExchangeRate(fromCurrency, ch.toCurrencyCode(r), 1.0, rates.get(r).doubleValue()))
                    .collect(Collectors.toCollection(ArrayList::new));

            //Adding values to cache
            exchangeRates.forEach(er -> cache.cacheExchangeRate(er.getFromCurrency(), er.getToCurrency(), 1.0, er.getToAmount()));

            } catch (Exception e) {
                e.printStackTrace();
            }

            return exchangeRates;
        }

}
