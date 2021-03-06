package com.edot.foodapp;

import android.content.Context;
import android.util.Log;

import com.edot.foodapp.data.ItemDataModel;
import com.edot.network.HttpGETClient;
import com.edot.network.HttpPOSTClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderHelper {

    public static boolean placeOrder(Context context,String userID,String hotelID,ArrayList<ItemDataModel> items,int total)
    {
        Gson gson = new Gson();
        ItemsListModel model = new ItemsListModel();
        model.arrayList = items;
        String orderDetails = gson.toJson(model);

        HttpPOSTClient httpPOSTClient = new HttpPOSTClient();
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("hotelID", hotelID);
        paramsMap.put("userID", userID);
        paramsMap.put("orderDetails", orderDetails);
        paramsMap.put("totalCost", String.valueOf(total));
        if (httpPOSTClient.establishConnection("http://autoiot2019-20.000webhostapp.com/FoodApp/makeOrder.php",paramsMap))
        {
            InputStream inputStream = httpPOSTClient.getInputStream();
            byte[] bytes = new byte[10];
            try {
                int length = inputStream.read(bytes);
                httpPOSTClient.closeConnection();
                String s = new String(bytes,0,length, StandardCharsets.UTF_8);
                Log.d(AppConstants.LOG_TAG,"OrderHelper"+s);
                return Boolean.parseBoolean(s);
            } catch (IOException e) {
                Log.d(AppConstants.LOG_TAG,"Exception while reading data : "
                        +e.getLocalizedMessage());
            }
        }
        return false;
    }

    public static List<OrderedDataModel> getOrdersFromDB(Context context)
    {
        List<OrderedDataModel> orderList = null;
        HttpGETClient httpGETClient = new HttpGETClient();
        if (httpGETClient.establishConnection
                ("http://autoiot2019-20.000webhostapp.com/FoodApp/viewOrderList.php"))
        {
            InputStream inputStream = httpGETClient.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[500];
            int length;
            try {
                while ((length = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, length);
                }
                httpGETClient.closeConnection();
                String data = new String(outputStream.toByteArray(),StandardCharsets.UTF_8);
                if (!data.isEmpty()) {
                    orderList = new ArrayList<>();
                    for (String s : data.split("##")) {
                        OrderedDataModel order = new OrderedDataModel();
                        String[] s1 = s.split("#");
                        order.orderId = Integer.parseInt(s1[0]);
                        order.hotelId = s1[1];
                        order.userId = s1[2];
                        order.orderedItems = s1[3];
                        order.netCost = Integer.parseInt(s1[4]);
                        orderList.add(order);
                    }
                    return orderList;
                }
            } catch (IOException e)
            {
                Log.d(AppConstants.LOG_TAG,"Exception while reading data : "
                        +e.getLocalizedMessage());
            }
        }
        return orderList;
    }

    public static class OrderedDataModel
    {
        public int orderId;
        public String hotelId;
        public String userId;
        public String orderedItems;
        public int netCost;
    }

    public static class ItemsListModel
    {
        public ArrayList<ItemDataModel> arrayList;
    }

}
