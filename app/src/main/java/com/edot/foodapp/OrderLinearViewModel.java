package com.edot.foodapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edot.models.LinearViewModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OrderLinearViewModel extends LinearViewModel {

    public static final List<String> ATTR_LIST = Arrays.asList(
            "name",
            "city",
            "cost",
            "items",
            "userId"
    );

    protected OrderLinearViewModel(Context context) {
        super(context);
    }

    @Override
    public View renderChildMap(HashMap<String, String> hashMap) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_order_view,null,false);

        TextView textView = view.findViewById(R.id.orderHotelNameView);
        textView.setText(hashMap.get(ATTR_LIST.get(0)));
        textView = view.findViewById(R.id.orderHotelCityView);
        textView.setText(hashMap.get(ATTR_LIST.get(1)));
        textView = view.findViewById(R.id.orderNetCostView);
        textView.setText(hashMap.get(ATTR_LIST.get(2)));
        textView = view.findViewById(R.id.orderItemView);
        textView.setText(hashMap.get(ATTR_LIST.get(3)));
        textView = view.findViewById(R.id.orderUserIDView);
        textView.setText(hashMap.get(ATTR_LIST.get(4)));

        return view;
    }
}
