package infrastructure.persistence.cache;

import domain.entities.ExchangeRate;
import domain.enums.CurrencyCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public interface ICache {


    boolean hasCachedExchangeRate(CurrencyCode from, CurrencyCode to, Double fromAmount);
    ExchangeRate getCachedExchangeRate(CurrencyCode from, CurrencyCode to, Double fromAmount);
    void cacheExchangeRate(CurrencyCode from, CurrencyCode to, Double fromAmount, Double toAmount);
    void cleanOutdatedCachedExchangeRates();
}
