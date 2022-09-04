package com.example.mbanking.ui.navigation

sealed class AppScreenNames(var route : String){
    object LogInScreen : AppScreenNames("log_in_screen")
    object RegistrationScreen : AppScreenNames("registration_screen")

    object AccountScreen : AppScreenNames("account_screen")


}
