package com.example.mbanking.viewModel

import android.content.Context
import android.provider.Settings.Global
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.example.mbanking.database.UserDatabase
import com.example.mbanking.database.model.Account
import com.example.mbanking.database.model.Transaction
import com.example.mbanking.ui.navigation.AppScreenNames
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



class AccountViewModel(val dispatcher: CoroutineDispatcher = Dispatchers.IO ,
                       val navController: NavController,
                    context : Context
) : ViewModel() {

    val TAG = "AccountViewModel"

    private val dao = UserDatabase.getInstance(context).schoolDao

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> get() = _accounts

    private val _tempAccount = MutableStateFlow<Account>(Account())
    val tempAccount: StateFlow<Account> get() = _tempAccount

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> get() = _transactions


    fun getData(userID : Int){
        getAccountsForUserId(userID)
        getTransactionsForAccount(_tempAccount.value)

    }

    private fun sortTransactionsByDate() {
        _transactions.value = _transactions.value.sortedByDescending { it.date }

        Log.i(TAG , "SORTED : _transactions.value : ${_transactions.value}")
    }

    private fun getAccountsForUserId(userId : Int) {

        val job = viewModelScope.launch(dispatcher) {

            try {
                _accounts.value = dao.getAccountsForUserId(userId)

                if(_tempAccount.value == Account())
                    _tempAccount.value = _accounts.value[0]
            }
            catch(e: Exception) {
                Log.i("User Tag logInUser()" ,"Exception :" + e.localizedMessage)
            }
        }

        runBlocking {
            job.join()
        }

        Log.i(TAG , "_accounts.value : ${_accounts.value}")
        Log.i(TAG , "_tempAccount.value : ${_tempAccount.value}")

    }

    private fun getTransactionsForAccount(account : Account) {

        viewModelScope.launch(dispatcher) {

            try {
                _transactions.value = dao.getTransactionsForAccountId(account.id)
                sortTransactionsByDate()
            }
            catch(e: Exception) {
                Log.i("User Tag logInUser()" ,"Exception :" + e.localizedMessage)
            }
        }

        Log.i(TAG , "_transactions.value : ${_transactions.value}")

    }

    fun addAccount(account : Account){

        val job = GlobalScope.launch(Dispatchers.Default) {

            try {
                dao.insertAccount(account)
            }
            catch(e: Exception) {
                Log.i("User Tag logInUser()" ,"Exception :" + e.localizedMessage)
            }
        }

        runBlocking {
            job.join()
        }


    }

    fun setTempAccount(account : Account){
        _tempAccount.value = account
    }


    fun logOut() {
        _accounts.value = emptyList()
        _tempAccount.value = Account()
        _transactions.value = emptyList()
    }

}