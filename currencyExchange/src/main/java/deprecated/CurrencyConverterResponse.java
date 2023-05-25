package deprecated;

public class CurrencyConverterResponse {
    private Double result;
    private String currencyFrom;
    private String currencyTo;
    private Number amount;

    public CurrencyConverterResponse(Double result, String currencyFrom, String currencyTo, Number amount) {
        this.result = result;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.amount = amount;
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

    public Number getAmount(){
        return amount;
    }

    public void setAmount(){
        this.amount = amount;
    }
}
