package com.example.mbankingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mbankingapp.adapters.MyListAdapter;
import com.example.mbankingapp.database.MainBankingDatabaseHandler;
import com.example.mbankingapp.model.Account;
import com.example.mbankingapp.model.Transaction;
import com.example.mbankingapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class AccountManagementActivity extends AppCompatActivity
{


    // Views

    private ListView list;

    private TextView textViewAccountNumber , textViewNameAndSurname;

    // Primitives

    private String[] dates , descriptions , amounts , types;

    // Operations

    private Intent intent;

    // Objects and Lists

    private List<Transaction> listTransactions;

    private User user;


    // Database

    private MainBankingDatabaseHandler mainBankingDatabaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        user = new User();

        intent = getIntent();

        String start = intent.getStringExtra("start");

        // Views
        initializeViewObjects();
        initializeMainDatabaseHandler();

        if(start.equals("Registration"))
        {
            createUserObject();
            saveUserToDatabase();

            if(isThereOnlyOneUserInTable())
            {
                connectAccountAndTransactionDataToUser();
            }

        }
        else
        {
            int userID = intent.getIntExtra("id" , -1);

            user = mainBankingDatabaseHandler.getUserTableHandler().getUserById(userID);

        }





        mainBankingDatabaseHandler.getUserTableHandler().printUserTable();


        setUsersAccounts(mainBankingDatabaseHandler , user);
        getTransactionsList(mainBankingDatabaseHandler , user);


        initializingTransactionArrays(listTransactions);
        setTransactionsArraysValues(listTransactions);

        // Adapter

        setTextViewValues(user);
        setListAdapter();



    }


    private boolean isThereOnlyOneUserInTable()
    {
        return mainBankingDatabaseHandler.getUserTableHandler().isThereOnlyOneUserInTable();
    }

    // Views

    private void initializeViewObjects()
    {
        textViewAccountNumber = findViewById(R.id.textViewAccountNumber);
        textViewNameAndSurname =  findViewById(R.id.textViewNameAndSurname);
    }

    private void setTextViewValues(User user)
    {
        textViewAccountNumber.setText("Account : " + user.getTempAccount().getId());
        textViewNameAndSurname.setText(user.getName() + " " + user.getSurname());
    }

    private void initializeMainDatabaseHandler()
    {
        mainBankingDatabaseHandler = new MainBankingDatabaseHandler(this);
    }

    // Database

    private void saveUserToDatabase()
    {

        user = mainBankingDatabaseHandler.getUserTableHandler().addUser(user);

        Log.i("INFO1231" , "User after saving in DB : " + user.getId() + " " + user.getName());
    }

    private void connectAccountAndTransactionDataToUser()
    {
        Log.i("INFO" , user.getId() + " " + user.getName() + " " + user.getSurname());
        mainBankingDatabaseHandler.downloadAndSaveBankingDataToUser(user);
        mainBankingDatabaseHandler.printAllTables();
    }



    private void createUserObject()
    {
        intent = getIntent();

        String name = intent.getStringExtra("name");
        String surname = intent.getStringExtra("surname");
        String pin = intent.getStringExtra("pin");

        user = new User(name , surname , Integer.valueOf(pin));
    }

    private void setUsersAccounts(MainBankingDatabaseHandler mainBankingDatabaseHandler , User user)
    {
        user.setListAccounts(mainBankingDatabaseHandler.getAccountTableHandler().getAllAccountsByUsersID(user.getId()));
    }


    private void getTransactionsList(MainBankingDatabaseHandler mainBankingDatabaseHandler , User user)
    {
        listTransactions =
                mainBankingDatabaseHandler
                .getTransactionTableHandler()
                .getAllTransactionsByAccountId
                        (user.getTempAccount().getId());
    }


    private void initializingTransactionArrays(List<Transaction> listTransactions)
    {
        int size = listTransactions.size();

        dates = new String[size];
        descriptions = new String[size];
        amounts = new String[size];
        types = new String[size];
    }

    private void setTransactionsArraysValues(List<Transaction> listTransactions)
    {
        int size = listTransactions.size();

        for(int i = 0; i < size; ++i)
        {
            dates[i] = listTransactions.get(i).getDateOfTransaction().toString();
            descriptions[i] = listTransactions.get(i).getDescription();
            amounts[i] = (listTransactions.get(i).getAmount() + " " +
                    listTransactions.get(i).getCurrency());
            types[i] = listTransactions.get(i).getType();
        }
    }
    private void setListAdapter()
    {
        MyListAdapter adapter=new MyListAdapter(this, dates , descriptions ,
                amounts , types);

        list = findViewById(R.id.listViewTransactions);
        list.setAdapter(adapter);
    }

    public void clickButtonLogOut(View view)
    {
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    }

    public void clickButtonChangeAccount(View view)
    {
        alertDialogChooseAccount(this);
    }

    private void alertDialogChooseAccount(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an Account");


                String[] accounts = user.getStringArrayOfAccounts();

                builder.setItems(accounts, (dialog, clickedAccountID) -> {

                    user.getTempAccount().setId(++clickedAccountID);

                    //Log.i("INFO123" , "Clicked Item " + clickedAccountID);
                    //Log.i("INFO123" , "Temp id : " + user.getTempAccount().getId());


                    getTransactionsList(mainBankingDatabaseHandler , user);


                    initializingTransactionArrays(listTransactions);
                    setTransactionsArraysValues(listTransactions);

                    Log.i("INFO123" , listTransactions.toString());

                    textViewAccountNumber.setText("Account " + (clickedAccountID));
                });

                AlertDialog dialog = builder.create();
                dialog.show();
    }
}