package infrastructure.persistence.cache;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CacheImpl implements ICache {
    ArrayList<CachedExchangeRate> cachedExchangeRates = new ArrayList<CachedExchangeRate>();

    @Override
    public boolean hasCachedExchangeRate(CurrencyCode from, CurrencyCode to, Double fromAmount) {
        cleanOutdatedCachedExchangeRates(); //Clears out data that's been on the cache for more that 1 minute
        //Looks on the ArrayList for the currency code that was requested and returns what was asked
        return cachedExchangeRates.stream().anyMatch(r ->
            r.getExchangeRate().getFromCurrency().equals(from)
                && r.getExchangeRate().getToCurrency().equals(to)
                && r.getExchangeRate().getFromAmount().equals(fromAmount)
        );
    }

    @Override
    public ExchangeRate getCachedExchangeRate(CurrencyCode from, CurrencyCode to, Double fromAmount) {
        //Filtering out the results, using .stream() to sequence the data and then looking for what the user inputed on cache
        //defining the orElse to return null if the value was not found

        CachedExchangeRate firstResult = cachedExchangeRates.stream().filter(r ->
            r.getExchangeRate().getFromCurrency().equals(from)
                && r.getExchangeRate().getToCurrency().equals(to)
                && r.getExchangeRate().getFromAmount().equals(fromAmount)
        ).findFirst().orElse(null);

        //it returs the first valid result if it is not null or a null value if that has happened
        return firstResult != null ? firstResult.getExchangeRate() : null;
    }

    @Override
    public void cacheExchangeRate(CurrencyCode from, CurrencyCode to, Double fromAmount, Double toAmount) {
        //Adds to the ArrayList the value that was received from the http request
        //Adds the exchange rate and the current LocalDateTime
        cachedExchangeRates.add(
            new CachedExchangeRate(
                LocalDateTime.now(),
                new ExchangeRate(from, to, fromAmount, toAmount)
            )
        );
    }

    @Override
    public void cleanOutdatedCachedExchangeRates() {
        //Checks if the on cache value has been there for at least one minute, and if so, clears the value out
        for (CachedExchangeRate cer : cachedExchangeRates) {
            if (Duration.between(cer.getCachedAt(), LocalDateTime.now()).toMinutes() >= 1) {
                cachedExchangeRates.remove(cer);
                break;
            }
        }
    }

}
