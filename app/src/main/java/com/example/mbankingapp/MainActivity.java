package com.example.mbankingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mbankingapp.database.MainBankingDatabaseHandler;
import com.example.mbankingapp.model.User;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainBankingDatabaseHandler mainBankingDatabaseHandler =
                new MainBankingDatabaseHandler(this);

        User user = new User();

        user.setName("Pera");
        user.setSurname("Peric");
        user.setPIN(6969);

        mainBankingDatabaseHandler.printAllTables();

        Log.i("INFO1234" ,"Status : "  +mainBankingDatabaseHandler.getUserTableHandler().isThereUser(user));

    }

    public void clickButtonLogin(View view)
    {
        intent = new Intent(this , LoginActivity.class);
        startActivity(intent);
    }

    public void clickButtonRegistration(View view)
    {
        intent = new Intent(this , RegistrationActivity.class);
        startActivity(intent);
    }
}