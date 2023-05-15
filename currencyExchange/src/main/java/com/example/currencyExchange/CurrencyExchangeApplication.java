package com.example.currencyExchange;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@SpringBootApplication
@ComponentScan("service")
public class CurrencyExchangeApplication {

	public static <JsonElement, JsonObject> void main(String[] args) throws IOException, ParseException {


		SpringApplication.run(CurrencyExchangeApplication.class, args);
		String urlStr = "https://api.exchangerate.host/convert?from=USD&to=EUR";


		URL url = new URL(urlStr);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		JSONParser jp = new JSONParser();
		JsonElement root = (JsonElement) jp.parse(new InputStreamReader((InputStream) request.getContent()));
		JSONObject jsonObject = new JSONObject((Map) root);
		String requestResult = jsonObject.get("result").toString();


		System.out.println("This is the exchange rate" + requestResult);


	}


}


