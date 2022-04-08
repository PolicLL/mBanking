package com.example.mbankingapp.database;

import android.content.Context;

import com.example.mbankingapp.converter.AccountJSONConverter;
import com.example.mbankingapp.converter.TransactionJSONConverter;
import com.example.mbankingapp.converter.UserJSONConverter;
import com.example.mbankingapp.database.tables.AccountTableHandler;
import com.example.mbankingapp.database.tables.TransactionTableHandler;
import com.example.mbankingapp.database.tables.UserTableHandler;
import com.example.mbankingapp.model.Account;
import com.example.mbankingapp.model.Transaction;
import com.example.mbankingapp.model.User;
import com.example.mbankingapp.service.DataDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainBankingDatabaseHandler
{
    private UserTableHandler userTableHandler;
    private AccountTableHandler accountTableHandler;
    private TransactionTableHandler transactionTableHandler;

    public MainBankingDatabaseHandler(Context context)
    {
        userTableHandler = new UserTableHandler(context);
        accountTableHandler = new AccountTableHandler(context);
        transactionTableHandler = new TransactionTableHandler(context);
    }

    public void deleteAllTables()
    {
        userTableHandler.dropTable();
        accountTableHandler.dropTable();
        transactionTableHandler.dropTable();
    }

    public void printAllTables()
    {
        userTableHandler.printUserTable();
        accountTableHandler.printAccountTable();
        transactionTableHandler.printTransactionTable();
    }

    public UserTableHandler getUserTableHandler() {
        return userTableHandler;
    }

    public AccountTableHandler getAccountTableHandler() {
        return accountTableHandler;
    }

    public TransactionTableHandler getTransactionTableHandler() {
        return transactionTableHandler;
    }

    private User user = new User();
    private Account account = new Account();
    private Transaction transaction = new Transaction();

    public void downloadAndSaveBankingDataToUser(User user)
    {
        DataDownloader dataDownloader = new DataDownloader();
        String data = dataDownloader.getAccountAndTransactionsData();

        this.user = user;

        try
        {
            // User JSON object

            JSONObject jsonObj = new JSONObject(data);

            UserJSONConverter userJSONConverter = new UserJSONConverter();

            //user = userJSONConverter.convertJsonObjectToUser(jsonObj);
            // bankingDatabaseHandler.getUserTableHandler().addUser(user);

            // Accounts Objects
            JSONArray jsonArrayAccounts = jsonObj.getJSONArray("acounts");

            for(int i = 0; i < jsonArrayAccounts.length(); ++i)
            {

                JSONObject tempJsonObject = jsonArrayAccounts.getJSONObject(i);


                AccountJSONConverter accountJSONConverter = new AccountJSONConverter();
                JSONArray jsonArrayTransactions = accountJSONConverter.getJsonArrayTransactions(tempJsonObject);
                account = accountJSONConverter.convertJsonObjectToAccount(tempJsonObject , user.getId());
                this.getAccountTableHandler().addAccount(account);


                for(int j = 0; j < jsonArrayTransactions.length(); ++j)
                {
                    JSONObject jsonObjectTransaction = jsonArrayTransactions.getJSONObject(j);

                    TransactionJSONConverter transactionJSONConverter = new TransactionJSONConverter();
                    transaction = transactionJSONConverter.convertJsonObjectToTransaction(jsonObjectTransaction , account);
                    this.getTransactionTableHandler().addTransaction(transaction);
                }

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
