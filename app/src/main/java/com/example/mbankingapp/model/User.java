package com.example.mbankingapp.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private int id , PIN;

    private int tempAccountID = -1;

    private String name , surname;

    private List<Account> listAccounts;

    // CONSTRUCTORS

    public User(int id, String name, String surname, int PIN)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.PIN = PIN;

        initializeListAccounts();
    }

    public User(String name, String surname, int PIN) {
        this.name = name;
        this.surname = surname;
        this.PIN = PIN;

        initializeListAccounts();
    }

    public User()
    {
        initializeListAccounts();
    }

    private void initializeListAccounts()
    {
        listAccounts = new ArrayList<>();
        tempAccountID = -1;
    }

    public void addAccount(Account account)
    {
        tempAccountID = 0;
        listAccounts.add(account);
    }

    public String[] getStringArrayOfAccounts()
    {
        if(listAccounts == null)
            return null;

        String[] accounts = new String[listAccounts.size()];

        for(int i = 0; i < accounts.length; ++i)
            accounts[i] = "Account " + (i + 1);

        return accounts;
    }

    // GETTERS & Setters

    public Account getTempAccount()
    {
        if(listAccounts.size() == 0)
            return new Account();

        return listAccounts.get(tempAccountID);
    }

    public List<Account> getListAccounts() {
        return listAccounts;
    }

    public void setListAccounts(List<Account> listAccounts) {
        this.tempAccountID = 0;
        this.listAccounts = listAccounts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPIN() {
        return PIN;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
    }

    // to String method

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", PIN=" + PIN +
                '}';
    }
}
