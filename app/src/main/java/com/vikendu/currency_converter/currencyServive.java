package com.vikendu.currency_converter;


import retrofit2.Call;
import retrofit2.http.GET;

public interface currencyServive {

    @GET("/api/v6/convert?q=USD_INR&compact=ultra")
    Call<currency> getExchange();


}
