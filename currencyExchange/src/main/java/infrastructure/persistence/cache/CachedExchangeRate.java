package infrastructure.persistence.cache;

import domain.entities.ExchangeRate;

import java.time.LocalDateTime;

public class CachedExchangeRate {
    //Saving the key atributes for the cached data: Exchange rate with all the atributes and the moment the it was cached
    private ExchangeRate exchangeRate;
    private LocalDateTime cachedAt; //Saves the year, month, day, hour, minutes and seconds with nanosecond precision

    public CachedExchangeRate(LocalDateTime cachedAt, ExchangeRate exchangeRate) {
        this.cachedAt = cachedAt;
        this.exchangeRate = exchangeRate;
    }

    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(ExchangeRate exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public LocalDateTime getCachedAt() {
        return cachedAt;
    }

    public void setCachedAt(LocalDateTime cachedAt) {
        this.cachedAt = cachedAt;
    }
}
