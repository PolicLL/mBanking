package com.example.mbanking.database.dao

import androidx.room.*
import com.example.mbanking.database.model.Account
import com.example.mbanking.database.model.User
import com.example.mbanking.database.model.Transaction

@Dao
interface UserDao {

    // User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user : User) : Long

    @Query("SELECT COUNT() FROM User")
    suspend fun getNumberOfUsersInTable() : Int

    @Query("SELECT * FROM user")
    suspend fun getAllUsers() : List<User>

    @Query("SELECT user_id FROM user WHERE firstName = :firstName AND secondName = :secondName AND pin = :pin")
    suspend fun logInUser(firstName : String , secondName : String , pin : String): Int

    // Account

    @Insert
    suspend fun insertAccount(account : Account) : Long

    @Query("SELECT * FROM account WHERE userId = :userId")
    suspend fun getAccountsForUserId(userId : Int): List<Account>

    // Transaction

    @Insert
    suspend fun insertTransaction(transaction: com.example.mbanking.database.model.Transaction)

    @Query("SELECT * FROM `Transaction` WHERE accountId = :accountID")
    suspend fun getTransactionsForAccountId(accountID : Int): List<Transaction>

}