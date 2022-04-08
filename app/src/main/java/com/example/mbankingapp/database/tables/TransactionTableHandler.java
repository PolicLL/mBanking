package com.example.mbankingapp.database.tables;

import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.ACCOUNT_COLUMN_ID;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.ACCOUNT_TABLE_NAME;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.DATABASE_NAME;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.DATABASE_VERSION;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.TRANSACTION_COLUMN_ID;
import static com.example.mbankingapp.database.mBankingDatabaseDataConstants.TRANSACTION_TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mbankingapp.model.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionTableHandler extends SQLiteOpenHelper
{
    // transaction_table TABLE COLUMNS


    private  final String TRANSACTION_COLUMN_DATE = "date";
    private  final String TRANSACTION_COLUMN_AMOUNT = "amount";
    private  final String TRANSACTION_COLUMN_CURRENCY = "currency";
    private  final String TRANSACTION_COLUMN_TYPE = "type";
    private  final String TRANSACTION_COLUMN_DESCRIPTION = "description";
    private  final String TRANSACTION_COLUMN_ACCOUNT_ID = "accountID";

    private AccountTableHandler accountTableHandler;


    public TransactionTableHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        accountTableHandler = new AccountTableHandler(context);

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        // account_table create command

        // id date amount currency type description accountID

        String  sqlUsersTable = "create table if not exists " +  TRANSACTION_TABLE_NAME +
                " ( " + TRANSACTION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + TRANSACTION_COLUMN_DATE + " date ,"
                + TRANSACTION_COLUMN_AMOUNT + " decimal ,"
                + TRANSACTION_COLUMN_CURRENCY + " varchar(255) , "
                + TRANSACTION_COLUMN_TYPE + " varchar(255) , "
                + TRANSACTION_COLUMN_DESCRIPTION + " varchar(255) , "
                + TRANSACTION_COLUMN_ACCOUNT_ID + " INT "
                + " , "
                + "FOREIGN KEY ( " + TRANSACTION_COLUMN_ACCOUNT_ID + " ) REFERENCES " + ACCOUNT_TABLE_NAME + " ( " + ACCOUNT_COLUMN_ID + ") );";

        sqLiteDatabase.execSQL(sqlUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        String sql = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void dropTable()
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
    }

    public boolean addTransaction(Transaction transaction)
    {
        return addTransaction(transaction.getDateOfTransaction() , transaction.getAmount() , transaction.getCurrency()
                                , transaction.getType() , transaction.getDescription() , transaction.getAccountId());
    }

    public boolean addTransaction(Date date , double amount , String currency , String type , String description , int accountID)
    {
        if(!accountTableHandler.isThereAccount(accountID))
            return false;

        ContentValues contentValues = new ContentValues();

        contentValues.put(TRANSACTION_COLUMN_DATE , String.valueOf(date));
        contentValues.put(TRANSACTION_COLUMN_AMOUNT , amount);
        contentValues.put(TRANSACTION_COLUMN_CURRENCY , currency);
        contentValues.put(TRANSACTION_COLUMN_TYPE , type);
        contentValues.put(TRANSACTION_COLUMN_DESCRIPTION , description);
        contentValues.put(TRANSACTION_COLUMN_ACCOUNT_ID , accountID);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(TRANSACTION_TABLE_NAME, null, contentValues) != -1;
    }

    public boolean deleteTransaction(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TRANSACTION_TABLE_NAME, TRANSACTION_COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    public Cursor getAllTransactions() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TRANSACTION_TABLE_NAME, null);
    }

    public List<Transaction> getAllTransactionsByAccountId(int accountId)
    {
        List<Transaction> listTransactions = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_COLUMN_ACCOUNT_ID + " = " + accountId;
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        int idIndex = cursor.getColumnIndex("id");
        int amountIndex = cursor.getColumnIndex("amount");
        int currencyIndex = cursor.getColumnIndex("currency");

        int dateIndex = cursor.getColumnIndex("date");
        int typeIndex = cursor.getColumnIndex("type");
        int descriptionIndex = cursor.getColumnIndex("description");
        int accountIDIndex = cursor.getColumnIndex("accountID");


        if (cursor.moveToFirst())
        {
            do
            {

                int tempID =  cursor.getInt(idIndex);
                double tempAmount = cursor.getDouble(amountIndex);
                String tempCurrency =  cursor.getString(currencyIndex);

                String tempDate = cursor.getString(dateIndex);
                String tempType = cursor.getString(typeIndex);
                String tempDescription = cursor.getString(descriptionIndex);
                int tempAccountIDIndex = cursor.getInt(accountIDIndex);

                Transaction transaction = new Transaction();

                transaction.setId(tempID);
                transaction.setAmount(tempAmount);
                transaction.setCurrency(tempCurrency);
                transaction.setDateOfTransaction(convertTransactionDateFromStringToDate(tempDate));
                transaction.setType(tempType);
                transaction.setDescription(tempDescription);
                transaction.setAccountId(tempAccountIDIndex);

                listTransactions.add(transaction);

            }
            while (cursor.moveToNext());
        }

        return listTransactions;
    }

    public Transaction getTransactionById(int id)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TRANSACTION_TABLE_NAME +
                " WHERE id = " + id +" ; " , null);

        cursor.moveToFirst();

        int idIndex = cursor.getColumnIndex("id");
        int amountIndex = cursor.getColumnIndex("amount");
        int currencyIndex = cursor.getColumnIndex("currency");
        int dateIndex = cursor.getColumnIndex("date");
        int typeIndex = cursor.getColumnIndex("type");
        int descriptionIndex = cursor.getColumnIndex("description");
        int accountIDIndex = cursor.getColumnIndex("accountID");

        Transaction tempTransaction = new Transaction();

        tempTransaction.setId(cursor.getInt(idIndex));
        tempTransaction.setAmount(cursor.getDouble(amountIndex));
        tempTransaction.setCurrency(cursor.getString(currencyIndex));


        tempTransaction.setDateOfTransaction
                (convertTransactionDateFromStringToDate(cursor.getString(dateIndex)));

        tempTransaction.setType(cursor.getString(typeIndex));
        tempTransaction.setDescription(cursor.getString(descriptionIndex));
        tempTransaction.setAccountId(cursor.getInt(accountIDIndex));

        return tempTransaction;
    }

    public void printTransactionTable()
    {
        Cursor cursor = getAllTransactions();

        int idIndex = cursor.getColumnIndex("id");
        int amountIndex = cursor.getColumnIndex("amount");
        int currencyIndex = cursor.getColumnIndex("currency");

        int dateIndex = cursor.getColumnIndex("date");
        int typeIndex = cursor.getColumnIndex("type");
        int descriptionIndex = cursor.getColumnIndex("description");
        int accountIDIndex = cursor.getColumnIndex("accountID");

        String header = String.format("%-15s %-15s %-15s %-45s %-15s %-15s %-15s" , TRANSACTION_COLUMN_ID , TRANSACTION_COLUMN_AMOUNT , TRANSACTION_COLUMN_CURRENCY ,
                TRANSACTION_COLUMN_DATE , TRANSACTION_COLUMN_TYPE , TRANSACTION_COLUMN_DESCRIPTION , TRANSACTION_COLUMN_ACCOUNT_ID);

        Log.i("Output" , "TRANSACTION TABLE");

        Log.i("Output" , header);

        if (cursor.moveToFirst())
        {
            do
            {

                int tempID =  cursor.getInt(idIndex);
                double tempAmount = cursor.getDouble(amountIndex);
                String tempCurrency =  cursor.getString(currencyIndex);

                String tempDate = cursor.getString(dateIndex);
                String tempType = cursor.getString(typeIndex);
                String tempDescription = cursor.getString(descriptionIndex);
                int tempAccountIDIndex = cursor.getInt(accountIDIndex);

                String row = String.format("%-15s %-15s %-15s %-45s %-15s %-15s %-15s" , tempID , tempAmount , tempCurrency ,
                        tempDate , tempType , tempDescription , tempAccountIDIndex);

                Log.i("Output" , row);

            }
            while (cursor.moveToNext());
        }

        Log.i("Output" , "");
    }

    public boolean isThereTransactionWithID(int id)
    {
        String sql = "SELECT * FROM " + TRANSACTION_TABLE_NAME + " WHERE " + TRANSACTION_COLUMN_ID + " = " + id + ";";

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        int count = cursor.getCount();

        return count > 0;

    }

    private  Date convertTransactionDateFromStringToDate(String dateString)
    {

        Date date = null;

        try
        {
            SimpleDateFormat print = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
            date =print.parse(dateString);

        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }

        return date;
    }

}
