package com.edot.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.edot.models.HelperUtil;
import com.edot.network.HttpGETClient;

import java.util.HashMap;

public class HotelsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);

        Intent intent = getIntent();
        String RClassFieldName = (intent == null)? null :
                intent.getStringExtra(CityLinearViewModel.ATTR_LIST.get(0));
        if(RClassFieldName != null)
        {
            RClassFieldName = RClassFieldName.toLowerCase();
        }
        final String cityChosen = (intent == null)? null :
                intent.getStringExtra(CityLinearViewModel.ATTR_LIST.get(1));
        Log.d(AppConstants.LOG_TAG,"Id : " + RClassFieldName);
        Log.d(AppConstants.LOG_TAG,"Name : " + cityChosen);

        new AsyncTask<String,Void,View>(){
            @Override
            protected LinearLayout doInBackground(String... strings) {
                HttpGETClient httpGETClient = new HttpGETClient();
                if (httpGETClient.establishConnection("http://autoiot2019-20.000webhostapp.com/" +
                        "FoodApp/"+strings[0]+".properties")) {
                    HashMap<String, HashMap<String, String>> map = HelperUtil.readProperties(httpGETClient
                                    .getInputStream(), HotelLinearViewModel.FIELD,
                            HotelLinearViewModel.ATTR_LIST);
                    httpGETClient.closeConnection();
                    LinearLayout l = (LinearLayout) new HotelLinearViewModel(HotelsPage.this)
                            .renderMap(map);
                    return l;
                }
                return null;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_hotels_page);
                TextView textView = findViewById(R.id.hotelTitleTextVView);
                textView.setText(getResources().getString(R.string.selectHotel,cityChosen));
                ScrollView scrollView = findViewById(R.id.hotelViewParentLayout);
                if (view == null)
                {
                    Toast.makeText(HotelsPage.this,R.string.somethingWentWrongCommon
                            ,Toast.LENGTH_SHORT).show();
                }
                else {
                    scrollView.addView(view);
                }
            }
        }.execute(RClassFieldName);

    }
}
