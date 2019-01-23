package com.edot.foodapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edot.foodapp.data.ItemDataModel;
import com.edot.foodapp.tables.OrderTable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrderHelper {

    public static boolean placeOrder(Context context,String userID,String hotelID,ArrayList<ItemDataModel> items,int total)
    {
        Gson gson = new Gson();
        ItemsListModel model = new ItemsListModel();
        model.arrayList = items;
        String orderDetails = gson.toJson(model);

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        SQLiteDatabase database = databaseHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OrderTable.HOTEL_ID, hotelID);
        values.put(OrderTable.USER_ID, userID);
        values.put(OrderTable.TOTAL_COST,total);
        values.put(OrderTable.ORDER_DETAILS,orderDetails);
        long resultCode = database.insert(OrderTable.TABLE_NAME, null, values);
        database.close();
        databaseHandler.close();

        return resultCode != -1;
    }

    public static List<OrderedDataModel> getOrdersFromDB(Context context)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        SQLiteDatabase database = databaseHandler.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+OrderTable.TABLE_NAME,null);

        List<OrderedDataModel> orderList = null;
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                orderList = new ArrayList<>();
                do {
                    OrderedDataModel order = new OrderedDataModel();
                    order.orderId = cursor.getInt(cursor.getColumnIndex(OrderTable.ORDER_ID));
                    order.hotelId = cursor.getString(cursor.getColumnIndex(OrderTable.HOTEL_ID));
                    order.userId = cursor.getString(cursor.getColumnIndex(OrderTable.USER_ID));
                    order.orderedItems = cursor.getString(cursor.getColumnIndex(OrderTable.ORDER_DETAILS));
                    order.netCost = cursor.getInt(cursor.getColumnIndex(OrderTable.TOTAL_COST));
                    orderList.add(order);
                } while(cursor.moveToNext());
                Log.d(AppConstants.LOG_TAG,"List of orders : "+orderList.toString());
            }
            cursor.close();
        }
        database.close();
        databaseHandler.close();
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
