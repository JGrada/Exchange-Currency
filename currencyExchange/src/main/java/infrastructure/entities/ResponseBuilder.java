package infrastructure.entities;

import java.util.Map;

public class ResponseBuilder {
    //Design Pattern Builder to create the response that will be shown on the endpoints
    private Response response = new Response();

    public ResponseBuilder setFrom(String from){
        response.setFrom(from);
        return this;
    }

    public ResponseBuilder setTo(String to){
        response.setTo(to);
        return this;
    }

    public ResponseBuilder setRate(Double rate){
        response.setRate(rate);
        return this;
    }

    public ResponseBuilder setAmount(Double amount){
        response.setAmount(amount);
        return this;
    }

    public ResponseBuilder setResult(Double result){
        response.setResult(result);
        return this;
    }

    public ResponseBuilder setRates(Map<String, Double> rates){
        response.setRates(rates);
        return this;
    }

    public Response build() {
        return response;
    }

}
