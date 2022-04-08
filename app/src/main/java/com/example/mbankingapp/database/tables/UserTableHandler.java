package com.example.mbankingapp.database.tables;

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

import com.example.mbankingapp.model.User;

public class UserTableHandler extends SQLiteOpenHelper
{

    private final String USER_COLUMN_NAME = "name";
    private final String USER_COLUMN_SURNAME = "surname";
    private final String USER_COLUMN_PIN = "pin";


    public UserTableHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        // Users table create command

        String  sqlUsersTable = "create table if not exists " +  USER_TABLE_NAME + " ( " + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
                " , " + USER_COLUMN_NAME + " varchar(255) ," + USER_COLUMN_SURNAME + " varchar(255) , "
                + USER_COLUMN_PIN + " INT);";

        sqLiteDatabase.execSQL(sqlUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        String sql = "DROP TABLE IF EXISTS " + USER_TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public void dropTable()
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "DROP TABLE IF EXISTS " + USER_TABLE_NAME + ";";
        sqLiteDatabase.execSQL(sql);
    }

    @SuppressLint("Range")
    public int isThereUser(User user)
    {
        int id = -1;



        String sql  = "SELECT * FROM " + USER_TABLE_NAME + " WHERE " +
                USER_COLUMN_NAME + " = " + "'" +user.getName() + "'" + " AND " +
                USER_COLUMN_SURNAME + " = " + "'" + user.getSurname() + "'"  +"AND " +
                USER_COLUMN_PIN + " = " +"'"+ user.getPIN() + "'" + ";";

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);


        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }


        return id;
    }

    public User addUser(User user)
    {

        long id = addUser(user.getName() , user.getSurname() , user.getPIN());

        User tempUser = this.getUserById(id);

        return tempUser;
    }

    public long addUser(String name, String surname, int pin)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_COLUMN_NAME, name);
        contentValues.put(USER_COLUMN_SURNAME, surname);
        contentValues.put(USER_COLUMN_PIN, pin);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(USER_TABLE_NAME, null, contentValues);
    }

    public long addUser(int id , String name, String surname, int pin)
    {
        ContentValues contentValues = new ContentValues();

        if(isThereUser(id))
           return 0;

        contentValues.put(USER_COLUMN_ID, id);
        contentValues.put(USER_COLUMN_NAME, name);
        contentValues.put(USER_COLUMN_SURNAME, surname);
        contentValues.put(USER_COLUMN_PIN, pin);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(USER_TABLE_NAME, null, contentValues);
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(USER_TABLE_NAME, USER_COLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null);
    }

    public boolean isUserTableEmpty()
    {
        String sql  = " SELECT * FROM " +  USER_TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        int count = cursor.getCount();

        return count == 0;
    }

    public boolean isThereOnlyOneUserInTable()
    {
        String sql  = " SELECT * FROM " +  USER_TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        int count = cursor.getCount();

        return count == 1;
    }

    public User getUserById(long id)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE_NAME +
                " WHERE id = " + id +" ; " , null);

        cursor.moveToFirst();

        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int surnameIndex = cursor.getColumnIndex("surname");
        int pinIndex = cursor.getColumnIndex("pin");

        User tempUser = new User();

        tempUser.setId(cursor.getInt(idIndex));
        tempUser.setName(cursor.getString(nameIndex));
        tempUser.setSurname(cursor.getString(surnameIndex));
        tempUser.setPIN(cursor.getInt(pinIndex));

        return tempUser;
    }

    public void printUserTable()
    {
        Cursor cursor = getAllUsers();

        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int surnameIndex = cursor.getColumnIndex("surname");
        int pinIndex = cursor.getColumnIndex("pin");

        String header = String.format("%-15s %-15s %-15s %-15s" , USER_COLUMN_ID , USER_COLUMN_NAME , USER_COLUMN_SURNAME , USER_COLUMN_PIN);

        Log.i("Output" , "USERS TABLE");

        Log.i("Output" , header);

        if (cursor.moveToFirst())
        {
            do
            {

                int tempID =  cursor.getInt(idIndex);
                String tempName = cursor.getString(nameIndex);
                String tempSurname =  cursor.getString(surnameIndex);
                int tempPin =  cursor.getInt(pinIndex);

                String row = String.format("%-15s %-15s %-15s %-15s" , tempID , tempName , tempSurname , tempPin);

                Log.i("Output" , row);
            }
            while (cursor.moveToNext());
        }


    }

    public boolean isThereUser(int id)
    {
        String sql  = "SELECT * FROM user_table WHERE id = " + id + ";";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();

        int count = cursor.getCount();

        return count > 0;
    }


}
