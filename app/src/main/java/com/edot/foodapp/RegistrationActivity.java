package com.edot.foodapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.edot.foodapp.tables.UserCredentials;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void registerUser(View view) {
        EditText nameView = findViewById(R.id.userName);
        EditText userIDView = findViewById(R.id.userRegisteredID);
        EditText passwordView = findViewById(R.id.userRegisteredPassword);

        String name = nameView.getText().toString();
        String userID = userIDView.getText().toString();
        String password = passwordView.getText().toString();

        if(name.isEmpty() || userID.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this,R.string.allMandatoryFields,Toast.LENGTH_SHORT).show();
        }
        else
        {
            if("admin".equals(userID))
            {
                Toast.makeText(this, R.string.userAlreadExists, Toast.LENGTH_SHORT).show();
            }
            else {
                DatabaseHandler databaseHandler = new DatabaseHandler(this);
                SQLiteDatabase database = databaseHandler.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(UserCredentials.NAME, name);
                values.put(UserCredentials.USER_ID, userID);
                values.put(UserCredentials.PASSWORD, password);
                long resultCode = database.insert(UserCredentials.TABLE_NAME, null, values);
                if (resultCode == -1) {
                    Toast.makeText(this, R.string.userAlreadExists, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.userAddedSuccessfully, Toast.LENGTH_SHORT).show();
                    database.close();
                    databaseHandler.close();
                    finish();
                }
            }
        }
    }
}
