package com.example.mbankingapp.model;

import androidx.annotation.NonNull;

import java.util.Date;

public class Transaction
{
    private int id , accountId;

    private Date dateOfTransaction;

    private String description, type , currency;

    private double amount;

    public Transaction()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        return " Transaction { " +
                " id = " + id +
                ", accountId = " + accountId +
                ", dateOfTransaction = " + dateOfTransaction +
                ", description = '" + description + '\'' +
                ", type = '" + type + '\'' +
                ", currency = '" + currency + '\'' +
                ", amount = " + amount +
                '}';
    }

}
