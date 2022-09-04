package com.example.mbanking.ui.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mbanking.ui.composables.AccountScreen
import com.example.mbanking.ui.composables.LogIn
import com.example.mbanking.ui.composables.Registration
import com.example.mbanking.viewModel.AccountViewModel
import com.example.mbanking.viewModel.LogInViewModel
import com.example.mbanking.viewModel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(context : Context){

    val navController = rememberNavController()

    val userViewModel = UserViewModel(context = context)

    val accountViewModel = AccountViewModel(context = context , navController = navController)

    val logInViewModel = LogInViewModel(context = context)

    NavHost(navController = navController, startDestination = AppScreenNames.LogInScreen.route){

        composable(route = AppScreenNames.LogInScreen.route){
            LogIn(navController , logInViewModel)
        }

        composable(route = AppScreenNames.RegistrationScreen.route){
            Registration(navController , userViewModel = userViewModel)
        }

        composable(route = AppScreenNames.AccountScreen.route + "/{userId}" ,
            arguments = listOf(navArgument("userId"){
                type = NavType.IntType
            })) {

            backStackEntry ->

            AccountScreen(accountViewModel ,
                userId = backStackEntry.arguments?.getInt("userId") ?: 0 , navController = navController)
        }
    }
}