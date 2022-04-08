package com.example.mbankingapp.model;

import androidx.annotation.NonNull;

public class Account
{
    private int id, userId;

    private String IBAN , Currency;

    private double amount;

    public Account(int id, int userId, String IBAN, String currency, double amount) {
        this.id = id;
        this.userId = userId;
        this.IBAN = IBAN;
        Currency = currency;
        this.amount = amount;
    }

    public Account()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @NonNull
    @Override
    public String toString() {
        return " Account { " +
                " id = " + id +
                ", userId = " + userId +
                ", IBAN = '" + IBAN + '\'' +
                ", Currency = '" + Currency + '\'' +
                ", amount = " + amount +
                '}';
    }
}
