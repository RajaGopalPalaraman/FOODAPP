package com.edot.foodapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.edot.foodapp.data.ItemDataModel;
import com.edot.models.HelperUtil;
import com.edot.network.HttpGETClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        new AsyncTask<Void,Void,View>()
        {
            @Override
            protected View doInBackground(Void... voids) {
                List<OrderHelper.OrderedDataModel> list= OrderHelper.getOrdersFromDB(OrderPage.this);
                if (list == null)
                {
                    return null;
                }
                HashMap<String,HashMap<String,String>> map = generateMap(list);
                View linearLayout = new OrderLinearViewModel(OrderPage.this).renderMap(map);
                return linearLayout;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_order_page);
                ScrollView scrollView = findViewById(R.id.orderViewParentLayout);
                if (view == null) {
                    Toast.makeText(OrderPage.this,R.string.noOrderFound,Toast.LENGTH_SHORT).show();
                    return;
                }
                scrollView.addView(view);
            }
        }.execute();
    }

    private HashMap<String,HashMap<String,String>> generateMap(List<OrderHelper.OrderedDataModel> list) {
        HashMap<String,HashMap<String,String>> map = new HashMap<>();
        HashMap<String,HashMap<String,String>> cityIDMap = new HashMap<>();
        HashMap<String,HashMap<String,String>> cities;

        HttpGETClient httpGETClient = new HttpGETClient();
        if (!httpGETClient.establishConnection("http://autoiot2019-20.000webhostapp.com/" +
                "FoodApp/cities.properties")) {
            return null;
        }
        cities = HelperUtil.readProperties(httpGETClient.getInputStream(),CityLinearViewModel.FIELD,
                CityLinearViewModel.ATTR_LIST);
        httpGETClient.closeConnection();

        for (String key : cities.keySet())
        {
            HashMap<String,String> city = cities.get(key);
            HashMap<String,String> newCity = new HashMap<>();
            newCity.put("name",city.get(CityLinearViewModel.ATTR_LIST.get(1)));
            cityIDMap.put(city.get(CityLinearViewModel.ATTR_LIST.get(0)).toLowerCase(),
                    newCity);
        }

        for (OrderHelper.OrderedDataModel order : list)
        {
            String cityId = order.hotelId.substring(0,order.hotelId.indexOf('h'));
            Log.d(AppConstants.LOG_TAG,"CityId : "+cityId);
            String hotelName="";
            String hotelCity="";
            HashMap<String,String> childMap = new HashMap<>();
            if (cityIDMap.containsKey(cityId))
            {
                HashMap<String,String> cityMap = cityIDMap.get(cityId);
                if (!cityMap.containsKey(order.hotelId)) {
                    HttpGETClient httpGETClient1 = new HttpGETClient();
                    if (!httpGETClient1.establishConnection("http://autoiot2019-20.000webhostapp.com/" +
                            "FoodApp/"+cityId+".properties")) {
                        return null;
                    }
                    HashMap<String,HashMap<String,String>> hotels = HelperUtil.readProperties(httpGETClient1.getInputStream(),
                            HotelLinearViewModel.FIELD,
                            HotelLinearViewModel.ATTR_LIST);
                    httpGETClient1.closeConnection();
                    for (String key:hotels.keySet())
                    {
                        HashMap<String,String> hotel = hotels.get(key);
                        cityMap.put(hotel.get(HotelLinearViewModel.ATTR_LIST.get(0)).toLowerCase()
                                ,hotel.get(HotelLinearViewModel.ATTR_LIST.get(1)));
                    }
                }
                hotelName = cityMap.get(order.hotelId);
                hotelCity = cityMap.get("name");
                Gson gson = new Gson();
                OrderHelper.ItemsListModel items= gson.fromJson(order.orderedItems,
                        OrderHelper.ItemsListModel.class);
                StringBuilder builder = new StringBuilder();
                boolean appenComma = false;

                for (ItemDataModel item: items.arrayList)
                {
                    if (appenComma)
                    {
                        builder.append(", ");
                    }

                    builder.append(item.getName());
                    builder.append(" X ");
                    builder.append(item.getQuantity());
                    appenComma = true;
                }

                childMap.put(OrderLinearViewModel.ATTR_LIST.get(0),hotelName);
                childMap.put(OrderLinearViewModel.ATTR_LIST.get(1),hotelCity);
                childMap.put(OrderLinearViewModel.ATTR_LIST.get(2),"Rs."+order.netCost);
                childMap.put(OrderLinearViewModel.ATTR_LIST.get(3),builder.toString());
                childMap.put(OrderLinearViewModel.ATTR_LIST.get(4),order.userId);
                childMap.put(OrderLinearViewModel.ATTR_LIST.get(5),order.timeStamp);

                map.put(String.valueOf(order.orderId),childMap);
            }
        }
        return map;
    }
}
