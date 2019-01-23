package com.edot.foodapp.tables;

import java.util.HashMap;

public class UserCredentials {

    public static final String TABLE_NAME = "UserCredentials";
    public static final String S_NO = "Sno";
    public static final String NAME = "Name";
    public static final String USER_ID = "UserID";
    public static final String PASSWORD = "Password";

    public static HashMap<String,String> getTableProperties()
    {
        HashMap<String,String> tableProperties = new HashMap<>();
        tableProperties.put(S_NO,"INTEGER PRIMARY KEY");
        tableProperties.put(NAME,"TEXT NOT NULL");
        tableProperties.put(USER_ID,"TEXT NOT NULL UNIQUE");
        tableProperties.put(PASSWORD,"TEXT NOT NULL");
        return tableProperties;
    }
}
