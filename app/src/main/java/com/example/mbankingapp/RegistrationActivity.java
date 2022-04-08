package com.example.mbankingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mbankingapp.database.MainBankingDatabaseHandler;
import com.example.mbankingapp.frontend.RegisterInputTest;

public class RegistrationActivity extends AppCompatActivity
{


    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPIN;

    private RegisterInputTest registerInputTest;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeViewReferences();

        registerInputTest = new RegisterInputTest();


       MainBankingDatabaseHandler mainBankingDatabaseHandler = new MainBankingDatabaseHandler(this);
       //mainBankingDatabaseHandler.deleteAllTables();

        //mainBankingDatabaseHandler.printAllTables();


    }


    private void initializeViewReferences()
    {
        editTextName = findViewById(R.id.editTextTextPersonName);
        editTextSurname = findViewById(R.id.editTextUserSurname);
        editTextPIN = findViewById(R.id.editTextPin);
    }


    private String textName , textSurname , pin;

    private Intent intent;

    public void clickRegisterButton(View view)
    {
        getTextValuesFromEditTexts();

        if(isDataInsertedCorrectly(textName , textSurname , pin))
        {

            intent = new Intent(this , AccountManagementActivity.class);

            saveDataToIntent(textName , textSurname , pin);

            startActivity(intent);
        }

        else
            showToastForWrongInformationInput();
    }

    private void getTextValuesFromEditTexts()
    {
        textName = editTextName.getText().toString();
        textSurname = editTextSurname.getText().toString();
        pin = editTextPIN.getText().toString();
    }


    private boolean isDataInsertedCorrectly(String textName , String textSurname , String pin)
    {
        return (registerInputTest.isInputCorrect(textName) && registerInputTest.isInputCorrect(textSurname)
                && registerInputTest.isPINCorrect(pin));
    }

    private void saveDataToIntent(String textName , String textSurname , String pin)
    {
        intent.putExtra("name" , textName);
        intent.putExtra("surname" , textSurname);
        intent.putExtra("pin" , pin);
        intent.putExtra("start" , "Registration");

    }

    private void showToastForWrongInformationInput()
    {
        Toast.makeText(this , "Some data is incorrect.", Toast.LENGTH_SHORT).show();
    }






}