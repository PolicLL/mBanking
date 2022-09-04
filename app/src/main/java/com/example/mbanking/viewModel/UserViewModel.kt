package com.example.mbanking.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mbanking.services.Api
import androidx.lifecycle.viewModelScope
import com.example.mbanking.database.UserDatabase
import com.example.mbanking.database.model.Account
import com.example.mbanking.database.model.Transaction
import com.example.mbanking.database.model.User
import com.example.mbanking.services.models.AccountJsonObject
import com.example.mbanking.services.models.TransactionJsonObject
import com.example.mbanking.services.models.UserJsonObject
import com.example.mbanking.viewModel.converters.AmountAndCurrencyConverter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

val TAG = "UserViewModel"

class UserViewModel(val dispatcher: CoroutineDispatcher = Dispatchers.IO ,
                    context : Context
) : ViewModel() {

    private val dao = UserDatabase.getInstance(context).schoolDao

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> get() = _user


    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> get() = _allUsers

    fun getAllUsers() {
        viewModelScope.launch(dispatcher) {
            try {
                _allUsers.value = dao.getAllUsers()
            }
            catch(e: Exception) {
                Log.i("User Tag getAllUsers()" ,"Exception :" + e.localizedMessage)
            }
        }
    }

    /**
    Function that takes user data from assecco endpoint
     */
    private fun getUserFromWeb() : UserJsonObject {

        var userJsonObject : UserJsonObject = UserJsonObject()

        val job = GlobalScope.launch(Dispatchers.Default) {
            try {
                userJsonObject = Api.retrofitService.getUser()
            }
            catch(e: Exception) {
                Log.i("User Tag logInUser()" ,"Exception :" + e.localizedMessage)
            }
        }

        runBlocking {
            job.join()
        }

        return userJsonObject
    }

    private fun addUser(user : User) : Int{

        var userID = 0

        val job = GlobalScope.launch(Dispatchers.Default) {

            try {
                userID = dao.insertUser(user).toInt()
            }
            catch(e: Exception) {
                Log.i("User Tag logInUser()" ,"Exception :" + e.localizedMessage)
            }
        }

        runBlocking {
            job.join()
        }

        return userID
    }

    fun registerUser(user : User) : Int {

        var userID = -1

        if(getNumberOfUsersInTable() == 0){
            userID = addUser(user)
            insertUserValuesFromWeb(userID)
        }

        else
            userID = addUser(user)

        return userID

    }

    fun isUserInformationInsertedCorrectly(user : User) : Boolean {
        return RegisterControl.registerControl.isUserInformationInsertedCorrectly(user)
    }


    private fun insertUserValuesFromWeb(userID : Int) {

        var userJsonObject = getUserFromWeb()

        var tempAccountId = 0

        val job = GlobalScope.launch(Dispatchers.Default) {
            try {

                for(account in userJsonObject.accounts!!){

                    var convertedAccount =
                        convertAccountJsonObjectToAccount(account , userID)

                    tempAccountId = dao.insertAccount(convertedAccount).toInt()


                    for(transaction in account.transactions!!){

                        var convertedTransaction =
                            convertTransactionJsonObjectToTransaction(transaction, tempAccountId)

                        dao.insertTransaction(convertedTransaction)
                    }

                }
            }
            catch(e: Exception) {
                Log.i("User Tag logInUser()" ,"Exception :" + e.localizedMessage)
            }
        }


        runBlocking {
            job.join()
        }


    }

    private fun convertTransactionJsonObjectToTransaction
                (transactionJsonObject: TransactionJsonObject , accountID : Int): Transaction {

        var transaction = Transaction()

        transaction.id = 0
        transaction.date = convertStringToDate(transactionJsonObject.date) // converting
        transaction.description = transactionJsonObject.description
        transaction.type = transactionJsonObject.type
        transaction.accountId = accountID

        transaction.amount = AmountAndCurrencyConverter()
            .splitIntoAmountAndCurrency(transactionJsonObject.amount)
            .amount

        transaction.currency = AmountAndCurrencyConverter()
            .splitIntoAmountAndCurrency(transactionJsonObject.amount)
            .currency

        return transaction
    }

    private fun convertAccountJsonObjectToAccount
                (accountJsonObject: AccountJsonObject, userID: Int): Account {

        var account = Account()

        account.id = accountJsonObject.id
        account.IBAN = accountJsonObject.IBAN
        account.currency = accountJsonObject.currency
        account.userId = userID

        account.amount = AmountAndCurrencyConverter().convertAccountsAmount(accountJsonObject.amount)

        return account
    }

    private fun getNumberOfUsersInTable() : Int{

        var count = 0

        val job = GlobalScope.launch(Dispatchers.Default) {
            try {
                count = dao.getNumberOfUsersInTable()
            }
            catch(e: Exception) {
                Log.i("User Tag logInUser()" ,"Exception :" + e.localizedMessage)
            }
        }

        runBlocking {
            job.join()
        }

        return count;

    }

    private fun convertStringToDate(textDate: String): Date {
        val formatter = SimpleDateFormat("dd.MM.yyyy.")

        return formatter.parse(textDate)
    }

}