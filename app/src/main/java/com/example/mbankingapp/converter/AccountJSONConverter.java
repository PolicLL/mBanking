package com.example.mbankingapp.converter;

import com.example.mbankingapp.model.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountJSONConverter
{
    private Account account;

    public AccountJSONConverter()
    {
        this.account = new Account();
    }

    public Account convertJsonObjectToAccount(JSONObject jsonObj , int userId) throws JSONException
    {
        account = new Account();

        int accountID = jsonObj.getInt("id");
        String IBAN = jsonObj.getString("IBAN");
        String amount = jsonObj.getString("amount");
        String currency = jsonObj.getString("currency");

        account.setId(accountID);
        account.setUserId(userId);
        account.setIBAN(IBAN);
        account.setAmount(convertAmountFromStringToDouble(amount));
        account.setCurrency(currency);

        return account;
    }

    public JSONArray getJsonArrayTransactions(JSONObject jsonObj) throws JSONException
    {

        return jsonObj.getJSONArray("transactions");
    }

    private  double convertAmountFromStringToDouble(String input)
    {
        String newString = "";

        for(int i = 0; i < input.length(); ++i)
        {
            char tempChar = input.charAt(i);

            if(tempChar == ',')
                tempChar = '.';

            else if(tempChar == '.') continue;

            newString += tempChar;

        }

        return Double.parseDouble(newString);
    }
}
