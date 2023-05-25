package infrastructure.persistence.cache;

import domain.entities.ExchangeRate;

import java.time.LocalDateTime;

public class CachedExchangeRate {
    private ExchangeRate exchangeRate;
    private LocalDateTime cachedAt;

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
