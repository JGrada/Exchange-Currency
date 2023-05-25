package deprecated;


import java.util.Map;


public class AllRatesResponse {
    private String baseCurrency;
    private Map rates;

    public AllRatesResponse(String baseCurrency, Map rates){
        this.baseCurrency = baseCurrency;
        this.rates = rates;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Map getRates() {
        return rates;
    }

    public void setRates(Map rates) {
        this.rates = rates;
    }
}
