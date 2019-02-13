package com.edot.foodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.edot.network.HttpPOSTClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText userID;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userID = findViewById(R.id.userID);
        password = findViewById(R.id.password);
    }

    private void openRegistrationPage() {
        Intent intent = new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }

    public void onLogin(final View view) {
        view.setClickable(false);
        if(!(userID.getText().toString().isEmpty() || password.getText().toString().isEmpty())) {
            Log.d(AppConstants.LOG_TAG, "@onLogin");
            if ("admin".equals(userID.getText().toString())) {
                if ("admin".equals(password.getText().toString())) {
                    Toast.makeText(this, R.string.adminLoginSuccess, Toast.LENGTH_SHORT).show();
                    AppConstants.currentLoggedInUserID = "admin";
                    AppConstants.currentLoggedInUserName = "admin";
                    resetAll();
                    Intent intent = new Intent(this,OrderPage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.invalidLoginCredentials, Toast.LENGTH_SHORT).show();
                    resetPassword();
                }
                view.setClickable(true);
            }
            else
            {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        HttpPOSTClient httpPOSTClient = new HttpPOSTClient();
                        HashMap<String,String> paramsMap = new HashMap<>();
                        paramsMap.put("userid",userID.getText().toString());
                        paramsMap.put("password",password.getText().toString());
                        if (httpPOSTClient.establishConnection("http://autoiot2019-20.000webhostapp.com/FoodApp/login.php",paramsMap))
                        {
                            InputStream inputStream = httpPOSTClient.getInputStream();
                            byte[] bytes = new byte[AppConstants.NAME_FIELD_SIZE];
                            try {
                                int length = inputStream.read(bytes);
                                String s = new String(bytes,0,length, StandardCharsets.UTF_8);
                                httpPOSTClient.closeConnection();
                                return s;
                            } catch (IOException e) {
                                Log.d(AppConstants.LOG_TAG,"Exception while reading data : "
                                        +e.getLocalizedMessage());
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        view.setClickable(true);
                        if (s == null || "401".equals(s))
                        {
                            Toast.makeText(LoginActivity.this, R.string.somethingWentWrong, Toast.LENGTH_SHORT).show();
                        }
                        else if ("null".equals(s))
                        {
                            Toast.makeText(LoginActivity.this, R.string.invalidLoginCredentials, Toast.LENGTH_SHORT).show();
                            resetPassword();
                        }
                        else
                        {
                            Log.d(AppConstants.LOG_TAG, s + " Logged in");
                            Toast.makeText(LoginActivity.this, R.string.loginSuccess, Toast.LENGTH_SHORT).show();
                            AppConstants.currentLoggedInUserID = userID.getText().toString();
                            AppConstants.currentLoggedInUserName = s;

                            resetAll();
                            Intent intent = new Intent(LoginActivity.this,CityPage.class);
                            startActivity(intent);
                        }
                    }
                }.execute();
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
