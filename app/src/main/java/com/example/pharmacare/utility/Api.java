package com.example.pharmacare.utility;

import com.example.pharmacare.model.Item;
import com.example.pharmacare.model.ItemBatch;
import com.example.pharmacare.model.Order;
import com.example.pharmacare.model.SellingType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    //    String HOST="https://localhost:";
    String PORT = ":35000/";
    String HOST = "http://192.168.8.118";
    String BASE_URL = HOST + PORT + "api/";

    @GET("orders")
    Call<List<Order>> getAllOrders();

    @GET("items/search?")
    Call<List<Item>> getItemByNameOrId(@Query("value") String page);

    @GET("itemBatch/search/item?")
    Call<List<ItemBatch>> getItemBatches(@Query("value") String itemName);

    @GET("sellingType")
    Call<List<SellingType>> getAllSellingTypes();
}
