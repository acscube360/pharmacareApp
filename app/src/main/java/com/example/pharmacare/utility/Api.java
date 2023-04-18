package com.example.pharmacare.utility;

import com.example.pharmacare.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    //    String HOST="https://localhost:";
    String PORT = ":35000/";
    String HOST = "http://192.168.8.118";
    String BASE_URL = HOST + PORT + "api/";

    @GET("orders")
    Call<List<Order>> getAllOrders();

    @GET("items/search?")
}
