package com.edot.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.edot.models.HelperUtil;

import java.util.HashMap;

public class ItemPage extends AppCompatActivity {

    public static final int REQ_CODE = 8;

    private ItemSelectionController controller;
    private String hotelID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        controller = new ItemSelectionController(this);
        Intent intent = getIntent();
        hotelID = (intent == null)? null : intent.getStringExtra(HotelLinearViewModel.ATTR_LIST.get(0));
        final String hotelName = (intent == null)? null : intent.getStringExtra(HotelLinearViewModel.ATTR_LIST.get(1));
        final String hotelComment = (intent == null)? null : intent.getStringExtra(HotelLinearViewModel.ATTR_LIST.get(2));

        Log.d(AppConstants.LOG_TAG,"Id : " + hotelID);
        Log.d(AppConstants.LOG_TAG,"Name : " + hotelName);
        Log.d(AppConstants.LOG_TAG,"Comment : "+hotelComment);

        if(hotelID != null)
        {
            hotelID = hotelID.toLowerCase();
        }
        Integer resourceID = (Integer) HelperUtil.getFieldFromClass(R.raw.class,null,hotelID);
        if(hotelName == null || hotelComment == null || resourceID == null)
        {
            Toast.makeText(this,R.string.error_404,Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d(AppConstants.LOG_TAG,"ResourceID : " + resourceID);
        new AsyncTask<Integer,Void,View>(){
            @Override
            protected LinearLayout doInBackground(Integer... integers) {
                HashMap<String,HashMap<String,String>> map = HelperUtil.readProperties(getResources()
                                .openRawResource(integers[0]),ItemLinearViewModel.FIELD,
                        ItemLinearViewModel.ATTR_LIST);
                LinearLayout l = (LinearLayout) new ItemLinearViewModel(ItemPage.this,controller)
                        .renderMap(map);
                return l;
            }

            @Override
            protected void onPostExecute(View view) {
                super.onPostExecute(view);
                setContentView(R.layout.activity_item_page);
                controller.setRootView((RelativeLayout)findViewById(R.id.itemPageRootLayout));
                View rootView = findViewById(R.id.itemPageRootLayout);
                TextView textView = findViewById(R.id.itemPagehotelTitleTextView);
                textView.setText(hotelName);
                textView = findViewById(R.id.itemPagehotelCommentTextView);
                textView.setText(hotelComment);
                LinearLayout scrollView = findViewById(R.id.itemViewParentLayout);
                scrollView.addView(view);
            }
        }.execute(resourceID);
    }

    public void navigateToBillPage(View view)
    {
        Log.d(AppConstants.LOG_TAG,"Proceed button clicked : "+AppConstants.currentLoggedInUserID);
        if(OrderHelper.placeOrder(this,AppConstants.currentLoggedInUserID,
                hotelID,controller.getListOfSelectedItems(),controller.getTotal())) {
            Toast.makeText(this, R.string.orderPlaced, Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this, R.string.orderFailed, Toast.LENGTH_SHORT).show();
        }
    }

}
