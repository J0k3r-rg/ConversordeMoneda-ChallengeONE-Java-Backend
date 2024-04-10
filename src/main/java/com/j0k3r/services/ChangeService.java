package com.j0k3r.services;

import com.google.gson.Gson;
import com.j0k3r.http.ExchangeRate;
import com.j0k3r.models.Exchanges;

import java.io.IOException;

public class ChangeService {

    private final ExchangeRate exchangeRate = new ExchangeRate();

    public Exchanges getExchangesRates(String currency) throws IOException, InterruptedException {
        System.out.println("Obteniendo tasas de cambio...");
        Gson gson = new Gson();
        return gson.fromJson(exchangeRate.getUrl(currency), Exchanges.class);
    }

    public double change(double amount, double rate){
        return amount * rate;
    }


}
