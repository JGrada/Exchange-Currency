package infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public class Response {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String from;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String to;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double rate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double amount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Double> rates;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }



}

