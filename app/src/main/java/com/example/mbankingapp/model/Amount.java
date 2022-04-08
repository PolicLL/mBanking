package com.example.mbankingapp.model;

import androidx.annotation.NonNull;

public class Amount
{
    private double amount;
    private String currency;

    public Amount()
    {

    }

    public Amount(double amount, String currency)
    {
        this.amount = amount;
        this.currency = currency;
    }

    public Amount(String textAmount)
    {
        Amount tempAmount = convertStringToAmount(textAmount);

        this.amount = tempAmount.getAmount();
        this.currency = tempAmount.getCurrency();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @NonNull
    @Override
    public String toString() {
        return " Money { " + " amount = " + amount + ", currency = " + currency + '}';
    }

    private static Amount convertStringToAmount(String moneyAmount)
    {
        Amount amount = new Amount();

        String sum = "";
        String currency = "";

        for(int i = 0; i < moneyAmount.length(); ++i)
        {
            char temp = moneyAmount.charAt(i);

            if((temp > 64) && (temp < 91))
                currency += temp;

            else
                sum += temp;
        }

        amount.setAmount(convertAmountFromStringToDouble(sum));
        amount.setCurrency(currency);

        return amount;

    }

    private static double convertAmountFromStringToDouble(String input)
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
