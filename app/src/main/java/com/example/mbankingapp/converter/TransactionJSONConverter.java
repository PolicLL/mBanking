package com.example.mbankingapp.converter;

import com.example.mbankingapp.model.Account;
import com.example.mbankingapp.model.Amount;
import com.example.mbankingapp.model.Transaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionJSONConverter
{
    private Transaction transaction;

    public TransactionJSONConverter()
    {
        this.transaction = new Transaction();
    }

    public Transaction convertJsonObjectToTransaction(JSONObject jsonObjectTransaction , Account account) throws JSONException
    {
        transaction = new Transaction();

        int transactionID = jsonObjectTransaction.getInt("id");
        String date = jsonObjectTransaction.getString("date");
        String description = jsonObjectTransaction.getString("description");
        String amountTransactionString = jsonObjectTransaction.getString("amount");
        int transactionAccountID = account.getId();

        Amount amountTrans = new Amount(amountTransactionString);

        transaction.setId(transactionID);
        transaction.setDateOfTransaction(convertTransactionDateFromStringToDate(date));
        transaction.setDescription(description);
        transaction.setAmount(amountTrans.getAmount());
        transaction.setCurrency(amountTrans.getCurrency());
        transaction.setAccountId(transactionAccountID);

        if(jsonObjectTransaction.has("type"))
            transaction.setType(jsonObjectTransaction.getString("type"));

        return transaction;
    }

    private Date convertTransactionDateFromStringToDate(String dateString)
    {

        Date date = null;

        try
        {
            date = new SimpleDateFormat("dd.MM.yyyy").parse(dateString);
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }

        return date;
    }

}
