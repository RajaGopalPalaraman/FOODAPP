package com.edot.foodapp;

import com.edot.foodapp.tables.OrderTable;
import com.edot.foodapp.tables.UserCredentials;

import java.util.HashMap;

public class AppConstants {

    public static final String LOG_TAG = "LOG_TAG";
    public static final String DATABASE_NAME = "Food";
    public static final int DATABASE_VERSION = 1;
    public static final HashMap<String,HashMap<String,String>> TABLE_LIST;

    public static String currentLoggedInUserID;

    static
    {
        TABLE_LIST = new HashMap<>();
        TABLE_LIST.put(UserCredentials.TABLE_NAME,UserCredentials.getTableProperties());
        TABLE_LIST.put(OrderTable.TABLE_NAME,OrderTable.getTableProperties());
    }

}
