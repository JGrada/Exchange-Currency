package service;

import java.util.ArrayList;
import java.util.Map;

public class SuppliedCurrencysResponse {
    private String base;
    private Map targetCurrencies;
    public SuppliedCurrencysResponse(String base, Map targetCurrencies) {
        this.base = base;
        this.targetCurrencies = targetCurrencies;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map getTargetCurrencies() {
        return targetCurrencies;
    }

    public void setTargetCurrencies(Map targetCurrencies) {
        this.targetCurrencies = targetCurrencies;
    }

}
