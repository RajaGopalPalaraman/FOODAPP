package com.edot.foodapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.edot.foodapp.tables.UserCredentials;

public class LoginActivity extends AppCompatActivity {

    private static final String SELECT_QUERY = "SELECT * FROM "+UserCredentials.TABLE_NAME +" WHERE "+UserCredentials.USER_ID+"=? AND "+UserCredentials.PASSWORD+"=?";

    private EditText userID;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userID = findViewById(R.id.userID);
        password = findViewById(R.id.password);
    }

    private void openOrderPage()
    {
        Intent intent = new Intent(this,OrderPage.class);
        startActivity(intent);
    }

    private void openCityPage()
    {
        Intent intent = new Intent(this,CityPage.class);
        startActivity(intent);
    }

    private void openRegistrationPage() {
        Intent intent = new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }

    public void onLogin(View view) {
        if(!(userID.getText().toString().isEmpty() || password.getText().toString().isEmpty())) {
            Log.d(AppConstants.LOG_TAG, "@onLogin");
            if ("admin".equals(userID.getText().toString())) {
                if ("admin".equals(password.getText().toString())) {
                    Toast.makeText(this, R.string.adminLoginSuccess, Toast.LENGTH_SHORT).show();
                    AppConstants.currentLoggedInUserID = "admin";
                    resetAll();
                    openOrderPage();
                } else {
                    Toast.makeText(this, R.string.invalidLoginCredentials, Toast.LENGTH_SHORT).show();
                    resetPassword();
                }
            } else {
                DatabaseHandler databaseHandler = new DatabaseHandler(this);
                SQLiteDatabase database = databaseHandler.getReadableDatabase();
                Cursor cursor = database.rawQuery(SELECT_QUERY, new String[]{userID.getText().toString(), password.getText().toString()});
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        String name = cursor.getString(cursor.getColumnIndex(UserCredentials.NAME));
                        Log.d(AppConstants.LOG_TAG, name + " Logged in");
                        Toast.makeText(this, R.string.loginSuccess, Toast.LENGTH_SHORT).show();
                        AppConstants.currentLoggedInUserID = userID.getText().toString();
                        resetAll();
                        openCityPage();
                    } else {
                        Toast.makeText(this, R.string.invalidLoginCredentials, Toast.LENGTH_SHORT).show();
                        resetPassword();
                    }
                    cursor.close();
                    database.close();
                    databaseHandler.close();
                } else {
                    Toast.makeText(this, R.string.somethingWentWrong, Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            Toast.makeText(this,R.string.loginFieldsEmpty,Toast.LENGTH_SHORT).show();
            resetPassword();
        }
    }

    public void onRegister(View view) {
        Log.d(AppConstants.LOG_TAG,"@onRegister");
        openRegistrationPage();
    }

    private void resetPassword()
    {
        password.setText("");
    }

    private void resetAll()
    {
        userID.setText("");
        password.setText("");
    }

}
