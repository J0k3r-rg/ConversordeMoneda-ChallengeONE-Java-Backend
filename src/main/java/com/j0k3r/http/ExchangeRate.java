package com.j0k3r.http;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeRate {

    private  String url ="https://v6.exchangerate-api.com/v6/API-KEY/latest/";

    public String getUrl(String currency) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url.replace("API-KEY", getApiKey())+currency))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString())
                .body();
    }

    private String getApiKey(){
        return  Dotenv.load().get("API_KEY");
    }

}
