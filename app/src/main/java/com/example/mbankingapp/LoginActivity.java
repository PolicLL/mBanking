package com.example.mbankingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mbankingapp.database.MainBankingDatabaseHandler;
import com.example.mbankingapp.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
    }

    private void initializeViews()
    {
        editTextName = findViewById(R.id.loginEditTextTextPersonName);
        editTextSurname = findViewById(R.id.loginEditTextUserSurname);
        editTextPin = findViewById(R.id.loginEditTextPin);
    }

    private String name, surname, pin;



    public void clickLoginButton(View view)
    {
        getDataFromLoginForm();

        User tempUser = new User(name , surname , Integer.valueOf(pin));

        int id = isUserInDataBase(tempUser);

        if(id != -1)
            intentToAccountManagementActivity(id);
        else
            printMessage();

    }

    private void getDataFromLoginForm()
    {
        name = editTextName.getText().toString();
        surname = editTextSurname.getText().toString();
        pin = editTextPin.getText().toString();
    }

    private void intentToAccountManagementActivity(int id)
    {
        Intent intent = new Intent(this , AccountManagementActivity.class);
        intent.putExtra("id" , id);
        intent.putExtra("start" , "Login");
        startActivity(intent);
    }

    private void printMessage()
    {
        Toast.makeText(getApplicationContext() , "There is no user like that." , Toast.LENGTH_LONG).show();
    }

    private int isUserInDataBase(User user)
    {
        MainBankingDatabaseHandler mainBankingDatabaseHandler =
                new MainBankingDatabaseHandler(this);

        return mainBankingDatabaseHandler.getUserTableHandler().isThereUser(user);

    }
}