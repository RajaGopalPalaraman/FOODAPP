package com.edot.foodapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.edot.models.HelperUtil;

import java.util.Arrays;
import java.util.HashMap;

public class CityPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        new AsyncTask<Integer,Void,View>(){
            @Override
            protected LinearLayout doInBackground(Integer... integers) {
                HashMap<String,HashMap<String,String>> map = HelperUtil.readProperties(getResources()
                                .openRawResource(integers[0]),CityLinearViewModel.FIELD,
                        CityLinearViewModel.ATTR_LIST);
                LinearLayout l = (LinearLayout) new CityLinearViewModel(CityPage.this)
                        .renderMap(map);
                return l;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_city_page);
                ScrollView scrollView = CityPage.this.findViewById(R.id.cityViewParentLayout);
                scrollView.addView(view);
            }
        }.execute(R.raw.cities);
    }
}
