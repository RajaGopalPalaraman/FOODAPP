package com.edot.foodapp.tables;

import java.util.HashMap;

public class OrderTable {
    public static final String TABLE_NAME = "OrderTable";
    public static final String ORDER_ID = "OrderID";
    public static final String HOTEL_ID = "HotelID";
    public static final String USER_ID = "UserID";
    public static final String ORDER_DETAILS = "OrderDetails";
    public static final String TOTAL_COST = "TotalCost";

    public static HashMap<String,String> getTableProperties()
    {
        HashMap<String,String> tableProperties = new HashMap<>();
        tableProperties.put(ORDER_ID,"INTEGER PRIMARY KEY");
        tableProperties.put(HOTEL_ID,"TEXT NOT NULL");
        tableProperties.put(USER_ID,"TEXT NOT NULL");
        tableProperties.put(ORDER_DETAILS,"TEXT NOT NULL");
        tableProperties.put(TOTAL_COST,"INTEGER");
        return tableProperties;
    }
}
