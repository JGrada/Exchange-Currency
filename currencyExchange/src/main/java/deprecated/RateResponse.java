package deprecated;

public class RateResponse {
    private Double result;
    private String currencyFrom;
    private String currencyTo;

    public RateResponse(Double result, String currencyFrom, String currencyTo) {
        this.result = result;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
    }



    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

}
