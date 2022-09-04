package com.example.mbanking.ui.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mbanking.R
import com.example.mbanking.viewModel.LogInViewModel
import com.example.mbanking.ui.navigation.AppScreenNames
import com.example.mbanking.ui.theme.Blue

@Composable
fun LogIn(navController : NavController, logInViewModel : LogInViewModel) {


    val focusManager = LocalFocusManager.current

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top)
    {

        LogInTitle(stringResource(R.string.app_title))

        Card(
            Modifier
                .weight(2f)
                .padding(24.dp),
            shape = RoundedCornerShape(32.dp),
            elevation = 4.dp
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(text = stringResource(R.string.welcome_back_log_in_screen_message) , fontSize = 24.sp , color = Blue)

                Column(Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {

                    Spacer(modifier = Modifier.weight(1f))

                    NameTextField(focusManager , stringResource(R.string.text_field_name_label) , logInViewModel)
                    SurnameTextField(focusManager , stringResource(R.string.text_field_surname_label) , logInViewModel)

                    Spacer(modifier = Modifier.height(8.dp))

                    PinTextField(focusManager , stringResource(R.string.text_field_pin_label) , logInViewModel)

                    Spacer(modifier = Modifier.height(16.dp))

                    LogInButton(navController , logInViewModel)

                    Spacer(modifier = Modifier.weight(1f))

                    RegistrationText(navController ,
                        stringResource(R.string.button_sign_up_text) ,
                        stringResource(R.string.button_forgot_password_text))

                }
            }
        }
    }
}

@Composable
fun LogInTitle(title : String) {
    Text(text = title,
        fontSize = 38.sp ,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(30.dp),
        fontStyle = FontStyle.Italic,
        color = Blue
    )
}


@Composable
fun NameTextField(focusManager : FocusManager, labelText : String, logInViewModel: LogInViewModel) {

    var name = logInViewModel.enteredName.collectAsState().value

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = {logInViewModel.setName(it)},
        label = { Text(text = labelText) },
        singleLine = true,
        trailingIcon = {
            if (name.isNotBlank())
                IconButton(onClick = { logInViewModel.setName("")}) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                }
        },

        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })

    )

}

@Composable
fun SurnameTextField(focusManager : FocusManager, labelText : String, logInViewModel: LogInViewModel) {

    var surname = logInViewModel.enteredSurname.collectAsState().value

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = surname,
        onValueChange = { logInViewModel.setSurname(it) },
        label = { Text(text = labelText) },
        singleLine = true,
        trailingIcon = {
            if (surname.isNotBlank())
                IconButton(onClick = { logInViewModel.setSurname("")}) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                }
        },

        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })

    )

}

@Composable
fun PinTextField(focusManager : FocusManager, labelText: String, logInViewModel: LogInViewModel) {



    var isPinVisible = logInViewModel.isPinVisible.collectAsState().value

    var pin = logInViewModel.enteredPin.collectAsState().value

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = pin,
        onValueChange = { logInViewModel.setPin(it) },
        label = { Text(text = labelText) },
        singleLine = true,
        visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),

        trailingIcon = {
            IconButton(onClick = { logInViewModel.setIsPinVisible(!isPinVisible) }) {
                Icon(
                    imageVector = if (isPinVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = ""
                )
            }
        },

        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password ,  imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })


    )

}

@Composable
fun LogInButton(navController: NavController , logInViewModel: LogInViewModel) {


    val context = LocalContext.current

    var formValidation = logInViewModel.isFormValid.collectAsState().value

    var toastMessageNoUser = stringResource(R.string.login_no_user_toast_message)

    var logInResponse = 0

    Button(
        onClick = {

            logInResponse = logInViewModel.logInUser()

            if(thereIsNoUserWithId(logInResponse))
                Toast.makeText(context , toastMessageNoUser , Toast.LENGTH_LONG).show()

            else {
                logInViewModel.clearUser()
                navController.navigate(AppScreenNames.AccountScreen.route + "/${logInResponse}")
            }


        },
        enabled = formValidation,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = stringResource(R.string.log_in_button_text))
    }

}

private fun thereIsNoUserWithId(id : Int) : Boolean{
    return id < 1
}

@Composable
fun RegistrationText(navController: NavController , signUpText : String , forgotPasswordText : String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = { navController.navigate(AppScreenNames.RegistrationScreen.route) }) {
            Text(text = signUpText , color = Blue)
        }
        TextButton(onClick = { }) {
            Text(text = forgotPasswordText, color = Color.Gray)
        }
    }
}

