package com.edot.foodapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.edot.network.HttpPOSTClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void registerUser(final View view) {

        view.setClickable(false);

        EditText nameView = findViewById(R.id.userName);
        EditText userIDView = findViewById(R.id.userRegisteredID);
        EditText emailView = findViewById(R.id.emailID);
        EditText passwordView = findViewById(R.id.userRegisteredPassword);
        EditText addressView = findViewById(R.id.address);
        EditText areaView = findViewById(R.id.area);
        EditText cityView = findViewById(R.id.city);
        EditText stateView = findViewById(R.id.state);

        final String name = nameView.getText().toString();
        final String userID = userIDView.getText().toString();
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();
        final String address = addressView.getText().toString();
        final String area = areaView.getText().toString();
        final String city = cityView.getText().toString();
        final String state = stateView.getText().toString();

        if(name.isEmpty() || userID.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty()
                || area.isEmpty() || city.isEmpty() || state.isEmpty()) {
            Toast.makeText(this,R.string.allMandatoryFields,Toast.LENGTH_SHORT).show();
        }
        else if ((name.length() > AppConstants.NAME_FIELD_SIZE))
        {
            Toast.makeText(this,getResources().getString(R.string.violationString,
                    getResources().getString(R.string.userName),AppConstants.NAME_FIELD_SIZE)
                    ,Toast.LENGTH_SHORT).show();
        }
        else if (!(userID.matches("\\d+") && userID.length() == 10))
        {
            Toast.makeText(this,getResources().getString(R.string.requiredString,
                    getResources().getString(R.string.userID),10)
                    ,Toast.LENGTH_SHORT).show();
        }
        else if (password.length() > 15)
        {
            Toast.makeText(this,getResources().getString(R.string.violationString,
                    getResources().getString(R.string.password),15)
                    ,Toast.LENGTH_SHORT).show();
        }
        else
        {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    HttpPOSTClient httpPOSTClient = new HttpPOSTClient();
                    HashMap<String,String> paramsMap = new HashMap<>();
                    paramsMap.put("userName",name);
                    paramsMap.put("userid",userID);
                    paramsMap.put("password",password);
                    paramsMap.put("email",email);
                    paramsMap.put("phone_no",userID);
                    paramsMap.put("address",address);
                    paramsMap.put("area",area);
                    paramsMap.put("city",city);
                    paramsMap.put("state",state);
                    if (httpPOSTClient.establishConnection("http://autoiot2019-20.000webhostapp.com/FoodApp/register.php",paramsMap))
                    {
                        InputStream inputStream = httpPOSTClient.getInputStream();
                        byte[] bytes = new byte[10];
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
                        Toast.makeText(RegistrationActivity.this, R.string.somethingWentWrong, Toast.LENGTH_SHORT).show();
                    }
                    else if (Boolean.parseBoolean(s))
                    {
                        Toast.makeText(RegistrationActivity.this, R.string.userAddedSuccessfully, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RegistrationActivity.this, R.string.userAlreadExists, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }
}