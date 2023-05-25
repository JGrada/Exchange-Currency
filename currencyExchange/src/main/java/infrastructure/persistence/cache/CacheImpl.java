package infrastructure.persistence.cache;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class CacheImpl implements ICache {
    @Override
    public boolean hasCachedExchangeRate(CurrencyCode from, CurrencyCode to, Double fromAmount) {
        cleanOutdatedCachedExchangeRates();
        return cachedExchangeRates.stream().anyMatch(r ->
            r.getExchangeRate().getFromCurrency().equals(from)
                && r.getExchangeRate().getToCurrency().equals(to)
                && r.getExchangeRate().getFromAmount().equals(fromAmount)
        );
    }

    @Override
    public ExchangeRate getCachedExchangeRate(CurrencyCode from, CurrencyCode to, Double fromAmount) {
        CachedExchangeRate firstResult = cachedExchangeRates.stream().filter(r ->
            r.getExchangeRate().getFromCurrency().equals(from)
                && r.getExchangeRate().getToCurrency().equals(to)
                && r.getExchangeRate().getFromAmount().equals(fromAmount)
        ).findFirst().orElse(null);

        return firstResult != null ? firstResult.getExchangeRate() : null;
    }

    @Override
    public void cacheExchangeRate(CurrencyCode from, CurrencyCode to, Double fromAmount, Double toAmount) {
        cachedExchangeRates.add(
            new CachedExchangeRate(
                LocalDateTime.now(),
                new ExchangeRate(from, to, fromAmount, toAmount)
            )
        );
    }

    @Override
    public void cleanOutdatedCachedExchangeRates() {
        for (CachedExchangeRate cer : cachedExchangeRates) {
            if (Duration.between(cer.getCachedAt(), LocalDateTime.now()).toMinutes() >= 1) {
                cachedExchangeRates.remove(cer);
                break;
            }
        }
    }

}
