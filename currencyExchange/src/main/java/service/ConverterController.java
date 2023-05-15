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

@RestController

public class ConverterController {
    @GetMapping("/api/convert")
    public  <JsonElement, JsonObject> String converter(@RequestParam String from,@RequestParam String to) throws IOException, ParseException {
        String urlStr = "https://api.exchangerate.host/convert?from=" + from + "&to=" + to;
        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JSONParser jp = new JSONParser();
        JsonElement root = (JsonElement) jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JSONObject jsonObject = new JSONObject((Map) root);
        String requestResult = jsonObject.get("result").toString();

        return requestResult;
    }
}
