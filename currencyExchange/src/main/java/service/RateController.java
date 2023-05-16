package service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

@RestController
public class RateController {
    @GetMapping("/api/viewRate")
    public <JsonElement> RateResponse rateController(@RequestParam String from, @RequestParam String to) throws IOException, ParseException {
        if (Objects.equals(from, "")) {
            from = "EUR";
        }
        if (Objects.equals(to, "")) {
            to = "USD";
        }
        System.out.println(from);
        System.out.println(to);
        String urlStr = "https://api.exchangerate.host/convert?from=" + from + "&to=" + to;
        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JSONParser jsonParser = new JSONParser();
        JsonElement jsonElement = (JsonElement) jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));


        Double result = (Double) ((Map<?, ?>) jsonElement).get("result");
        String currencyFrom = (String) ((JSONObject) ((Map<?, ?>) jsonElement).get("query")).get("from");
        String currencyTo = (String) ((JSONObject) ((Map<?, ?>) jsonElement).get("query")).get("to");

        return new RateResponse(result, currencyFrom, currencyTo);

    }
}
