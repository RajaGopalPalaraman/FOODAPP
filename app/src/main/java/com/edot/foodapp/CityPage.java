package com.edot.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.edot.models.HelperUtil;
import com.edot.network.HttpGETClient;

import java.util.Arrays;
import java.util.HashMap;

public class CityPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        new AsyncTask<Void,Void,View>(){
            @Override
            protected LinearLayout doInBackground(Void... integers) {
                HttpGETClient httpGETClient = new HttpGETClient();
                if (httpGETClient.establishConnection("http://autoiot2019-20.000webhostapp.com/" +
                        "FoodApp/cities.properties")) {
                    HashMap<String, HashMap<String, String>> map = HelperUtil.readProperties(httpGETClient.
                                    getInputStream(), CityLinearViewModel.FIELD,
                            CityLinearViewModel.ATTR_LIST);
                    httpGETClient.closeConnection();
                    LinearLayout l = (LinearLayout) new CityLinearViewModel(CityPage.this)
                            .renderMap(map);
                    return l;
                }
                return null;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_city_page);
                ScrollView scrollView = CityPage.this.findViewById(R.id.cityViewParentLayout);
                if (view == null)
                {
                    Toast.makeText(CityPage.this,R.string.somethingWentWrongCommon
                            ,Toast.LENGTH_SHORT).show();
                }
                else {
                    scrollView.addView(view);
                }
            }
        }.execute();
    }
    public void onHistory(View view)
    {
        Intent intent = new Intent(this,UserHistoryActivity.class);
        startActivity(intent);
    }
}
