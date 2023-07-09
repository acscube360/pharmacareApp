package com.example.pharmacare.utility;

import com.example.pharmacare.model.Item;
import com.example.pharmacare.model.ItemBatch;
import com.example.pharmacare.model.ItemCategory;
import com.example.pharmacare.model.ItemSubCategory;
import com.example.pharmacare.model.Order;
import com.example.pharmacare.model.OrderBatchItem;
import com.example.pharmacare.model.OrderItem;
import com.example.pharmacare.model.SellingType;
import com.example.pharmacare.model.Supplier;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

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

    @GET("orders/activeOrders")
    Call<List<Order>> getActiveOrders();

    @DELETE("orders/{id}")
    Call<JSONObject> deleteActiveOrder(@Path("id") int orderId);

    @GET("oderBatchItems")
    Call<List<OrderBatchItem>> getOrderBatchItems();

//    @GET("oderBatchItems?$filter=orderId eq orderID &$select=totalPrice,unitPrice,totalCost,quantity,id&$expand=itemBatch($expand=item($select=name,id))&$expand=sellingType($select=name)&$expand=itemBatch($select=item)")
//    Call<List<OrderItem>> getItemListOfOrder(@Query("orderID") int page);

    @GET
    Call<List<OrderItem>> getItemListOfOrder(@Url String s);

    @GET("items")
    Call<List<Item>> getAllItems();


    @GET("itemCategory")
    Call<List<ItemCategory>> getAllCategories();


    @GET("itemSubCategory")
    Call<List<ItemSubCategory>> getAllSubCategories();

    @GET("supplier")
    Call<List<Supplier>> getAllSuppliers();



}
