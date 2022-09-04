package com.example.mbanking.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.ui.input.key.Key.Companion.Sleep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mbanking.database.UserDatabase
import com.example.mbanking.services.Api
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LogInViewModel(val dispatcher: CoroutineDispatcher = Dispatchers.IO,
                     context : Context
) : ViewModel() {

    private val dao = UserDatabase.getInstance(context).schoolDao

    // Fields

    private val _enteredPin = MutableStateFlow("")
    val enteredPin: StateFlow<String> = _enteredPin

    private val _enteredName = MutableStateFlow("")
    val enteredName: StateFlow<String> = _enteredName

    private val _enteredSurname = MutableStateFlow("")
    val enteredSurname: StateFlow<String> = _enteredSurname

    // Visibility and validation

    private val _isPinVisible = MutableStateFlow(false)
    val isPinVisible: StateFlow<Boolean> = _isPinVisible

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid

    // Log In

    private val _logInResponse = MutableStateFlow(-1)
    val logInResponse: StateFlow<Int> = _logInResponse

    // Setters

    fun setIsPinVisible(state : Boolean){
        _isPinVisible.value = state
    }

    fun setPin(Pin : String){
        _enteredPin.value = Pin
        isFormValid()
    }

    fun setName(name : String){
        _enteredName.value = name
        isFormValid()
    }

    fun setSurname(surname : String){
        _enteredSurname.value = surname
        isFormValid()
    }

    private fun isFormValid(){
        _isFormValid.value = _enteredName.value.isNotEmpty() &&
                _enteredSurname.value.isNotEmpty() &&
                _enteredPin.value.isNotEmpty()
    }

    // Database

    fun logInUser() : Int {

        var response = 0

        val job = GlobalScope.launch(Dispatchers.Default) {
            try {
                response = dao.logInUser(enteredName.value , enteredSurname.value , enteredPin.value)
            }
            catch(e: Exception) {
                Log.i("User Tag logInUser()" ,"Exception :" + e.localizedMessage)
            }
        }

        runBlocking {
            job.join()
        }

        return response

    }

    fun clearUser() {
        _enteredName.value = ""
        _enteredSurname.value = ""
        _enteredPin.value = ""
    }

}