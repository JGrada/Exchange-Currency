package service;

import java.io.Serializable;

public class Request implements Serializable {
    private String currencyA;
    private String currencyB;
    private float amount;

    public String getCurrencyA() {
        return currencyA;
    }

    public void setCurrencyA(String currencyA) {
        this.currencyA = currencyA;
    }

    public String getCurrencyB() {
        return currencyB;
    }

    public void setCurrencyB(String currencyB) {
        this.currencyB = currencyB;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }


}
