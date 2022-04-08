package com.example.mbankingapp.database.tables;


import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.ACCOUNT_COLUMN_ID;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.ACCOUNT_TABLE_NAME;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.DATABASE_NAME;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.DATABASE_VERSION;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.USER_COLUMN_ID;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.USER_TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mbankingapp.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountTableHandler extends SQLiteOpenHelper
{
    // account_table TABLE COLUMNS

    private  final String ACCOUNT_COLUMN_IBAN = "iban";
    private  final String ACCOUNT_COLUMN_AMOUNT = "amount";
    private  final String ACCOUNT_COLUMN_CURRENCY = "currency";
    private  final String ACCOUNT_COLUMN_USERS_ID = "users_id";

    private UserTableHandler userTableHandler;

    public AccountTableHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        userTableHandler = new UserTableHandler(context);

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        // account_table create command

        String  sqlUsersTable = "create table if not exists " +  ACCOUNT_TABLE_NAME +
                " ( " + ACCOUNT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
                " , " + ACCOUNT_COLUMN_IBAN + " varchar(255) ,"
                      + ACCOUNT_COLUMN_CURRENCY + " varchar(255) , "
                      + ACCOUNT_COLUMN_AMOUNT + " DECIMAL ,"
                      + ACCOUNT_COLUMN_USERS_ID + " INT "
                      + " , "
                      + "FOREIGN KEY ( " + ACCOUNT_COLUMN_USERS_ID + " ) REFERENCES " + USER_TABLE_NAME + " ( " + USER_COLUMN_ID + ") );";

        sqLiteDatabase.execSQL(sqlUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        String sql = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void dropTable()
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
    }

    public boolean addAccount(Account account)
    {
        return addAccount(account.getId() , account.getIBAN() , account.getAmount() , account.getCurrency() , account.getUserId());
    }

    public boolean addAccount(int id , String iban , double amount , String currency , int users_id)
    {
        if(!userTableHandler.isThereUser(users_id))
            return false;

        if(isThereAccount(id))
            return false;

        ContentValues contentValues = new ContentValues();

        contentValues.put(ACCOUNT_COLUMN_ID , id);
        contentValues.put(ACCOUNT_COLUMN_IBAN , iban);
        contentValues.put(ACCOUNT_COLUMN_AMOUNT , amount);
        contentValues.put(ACCOUNT_COLUMN_CURRENCY , currency);
        contentValues.put(ACCOUNT_COLUMN_USERS_ID , users_id);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(ACCOUNT_TABLE_NAME, null, contentValues) != -1;
    }

    public boolean deleteAccount(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(ACCOUNT_TABLE_NAME, ACCOUNT_COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    public Cursor getAllAccounts() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + ACCOUNT_TABLE_NAME, null);
    }

    public List<Account> getAllAccountsByUsersID(int usersID)
    {
        List<Account> accountsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + ACCOUNT_COLUMN_USERS_ID + " = " + usersID;
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        int idIndex = cursor.getColumnIndex("id");
        int amountIndex = cursor.getColumnIndex("amount");
        int currencyIndex = cursor.getColumnIndex("currency");
        int usersIdIndex = cursor.getColumnIndex("users_id");
        int ibanIndex = cursor.getColumnIndex("iban");

        if (cursor.moveToFirst())
            do
            {

                int tempID =  cursor.getInt(idIndex);
                double tempAmount = cursor.getDouble(amountIndex);
                String tempCurrency =  cursor.getString(currencyIndex);
                String tempIBAN =  cursor.getString(ibanIndex);
                int tempUsersID = cursor.getInt(usersIdIndex);

                Account account = new Account();

                account.setId(tempID);
                account.setAmount(tempAmount);
                account.setCurrency(tempCurrency);
                account.setIBAN(tempIBAN);
                account.setUserId(tempUsersID);

                accountsList.add(account);

            }
            while (cursor.moveToNext());

        return accountsList;

    }

    public Account getAccountById(int id)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + ACCOUNT_TABLE_NAME +
                " WHERE id = " + id +" ; " , null);

        cursor.moveToFirst();

        // iban amount currency users_id

        int idIndex = cursor.getColumnIndex("id");
        int amountIndex = cursor.getColumnIndex("amount");
        int currencyIndex = cursor.getColumnIndex("currency");
        int usersIdIndex = cursor.getColumnIndex("users_id");
        int ibanIndex = cursor.getColumnIndex("iban");

        Account tempAccount = new Account();

        tempAccount.setId(cursor.getInt(idIndex));
        tempAccount.setAmount(cursor.getDouble(amountIndex));
        tempAccount.setCurrency(cursor.getString(currencyIndex));
        tempAccount.setUserId(cursor.getInt(usersIdIndex));
        tempAccount.setIBAN(cursor.getString(ibanIndex));

        return tempAccount;
    }

    public void printAccountTable()
    {
        Cursor cursor = getAllAccounts();

        int idIndex = cursor.getColumnIndex("id");
        int amountIndex = cursor.getColumnIndex("amount");
        int currencyIndex = cursor.getColumnIndex("currency");
        int usersIdIndex = cursor.getColumnIndex("users_id");
        int ibanIndex = cursor.getColumnIndex("iban");

        String header = String.format("%-15s %-15s %-15s %-25s %-15s" , ACCOUNT_COLUMN_ID , ACCOUNT_COLUMN_AMOUNT , ACCOUNT_COLUMN_CURRENCY ,
                        ACCOUNT_COLUMN_IBAN , ACCOUNT_COLUMN_USERS_ID);

        Log.i("Output" , "ACCOUNT TABLE");

        Log.i("Output" , header);

        if (cursor.moveToFirst())
        {
            do
            {

                int tempID =  cursor.getInt(idIndex);
                double tempAmount = cursor.getDouble(amountIndex);
                String tempCurrency =  cursor.getString(currencyIndex);
                String tempIBAN =  cursor.getString(ibanIndex);
                int tempUsersID = cursor.getInt(usersIdIndex);

                String row = String.format("%-15s %-15s %-15s %-25s %-25s" , tempID , tempAmount , tempCurrency ,
                        tempIBAN , tempUsersID);

                Log.i("Output" , row);

            }
            while (cursor.moveToNext());
        }

        Log.i("Output" , "");
    }


    public boolean isThereAccount(int id)
    {
        String sql = "SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + ACCOUNT_COLUMN_ID + " = " + id + ";";

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        int count = cursor.getCount();

        return count > 0;

    }



}

