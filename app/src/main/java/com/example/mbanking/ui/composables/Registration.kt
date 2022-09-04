package com.example.mbanking.ui.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.mbanking.R
import com.example.mbanking.database.model.User
import com.example.mbanking.ui.navigation.AppScreenNames
import com.example.mbanking.ui.theme.Blue
import com.example.mbanking.viewModel.UserViewModel


@Composable
fun Registration(navController : NavController , user : User = User() , userViewModel : UserViewModel) {

    val focusManager = LocalFocusManager.current

    Card(Modifier
        .padding(24.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = 4.dp){

        Box(modifier = Modifier.background(Color.White)){
            Column(){

                RegistrationTitle()
                RegistrationFormBody(navController ,focusManager , user , userViewModel)

            }
        }

    }

}

@Composable
fun RegistrationTitle() {
    Text(
        text = stringResource(R.string.registration_from_title),
        fontSize = 32.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = Blue,
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(10.dp)
    )
}

@Composable
fun RegistrationFormBody(navController : NavController,
                         focusManager : FocusManager ,
                         user : User ,
                         userViewModel : UserViewModel) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)){

        RegistrationForm(focusManager , user)
        RegistrationFormButtons(navController , user , userViewModel = userViewModel)
    }

}



@Composable
fun RegistrationForm(focusManager : FocusManager , user : User) {


    var firstName by remember { mutableStateOf("") }
    var secondName by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var isPinVisible by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(
        start = 16.dp, bottom = 8.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {


        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = firstName,
            onValueChange = {
                firstName = it
                user.firstName = it},
            label = { Text(text = stringResource(R.string.registration_text_field_name_label)) },
            singleLine = true,
            trailingIcon = {
                if (firstName.isNotBlank())
                    IconButton(onClick = {firstName = ""}) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                    }
            },

            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })

        )


        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = secondName,
            onValueChange = {
                secondName = it
                user.secondName = it},
            label = { Text(text = stringResource(R.string.registration_text_field_surname_label)) },
            singleLine = true,
            trailingIcon = {
                if (secondName.isNotBlank())
                    IconButton(onClick = {secondName = ""}) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                    }
            },

            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })

        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = pin,
            onValueChange = {
                pin = it
                user.pin = it},
            label = { Text(text = stringResource(R.string.registration_text_field_pin_label)) },
            singleLine = true,
            visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),

            trailingIcon = {
                IconButton(onClick = { isPinVisible = !isPinVisible }) {
                    Icon(
                        imageVector = if (isPinVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "content description"
                    )
                }
            },

            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword,  imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }))

    }

}


@Composable
fun RegistrationFormButtons(navController : NavController, user : User, userViewModel: UserViewModel) {

    val context = LocalContext.current

    var signInWrongInputMessage = stringResource(R.string.sign_in_wrong_input_message)

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp) ,
        horizontalArrangement = Arrangement.SpaceBetween){

        ControlButton(stringResource(R.string.sign_in_button_text)) {

            if(userViewModel.isUserInformationInsertedCorrectly(user)){
                var userID = userViewModel.registerUser(user)

                user.clearValues()
                navController.navigate(AppScreenNames.AccountScreen.route + "/${userID}")
            }
            else
                Toast.makeText(context , signInWrongInputMessage , Toast.LENGTH_LONG).show()

        }
        ControlButton(stringResource(R.string.cancel_button_text)) {}
    }

}

@Composable
fun ControlButton(buttonText : String , onClick: () -> Unit) {

    Button(onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Blue)) {
        Text(text = buttonText , color = Color.White)
    }

}


@Preview
@Composable
fun Test() {

}