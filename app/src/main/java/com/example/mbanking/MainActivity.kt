package com.example.mbanking

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mbanking.database.model.Account
import com.example.mbanking.services.Api
import com.example.mbanking.services.ApiService
import com.example.mbanking.ui.navigation.Navigation
import com.example.mbanking.ui.theme.MBankingTheme
import com.example.mbanking.viewModel.UserViewModel
import kotlinx.coroutines.runBlocking
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MBankingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    
                     Navigation(context = this)

                    /*
                    var textDate = "25.01.2016."
                    var textDate2 = "25-01-2016"

                     */

                    //var date = LocalDate.parse(textDate)
                    //var date2 = LocalDate.parse(textDate2)

                    //Log.i("MainActivityInf" , date.toString())
                    //Log.i("MainActivityInf" , date2.toString())

                    /*
                    runBlocking {
                        Log.i("Test Service" , Api.retrofitService.getUser().toString())
                    }
                     */

                }
            }
        }
    }
}

private fun convertStringDateToDateObject(stringDate : String) : Date?{
    var date: Date? = null

    try {

        val print = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy")
        date = print.parse(stringDate)

    } catch (ex: ParseException) {
        ex.printStackTrace()
    }

    return date

}

@Composable
fun InputDialogView(onDismiss:() -> Unit) {

    val list = listOf(Account() , Account())

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = 8.dp
        ) {
            Column(
                Modifier.background(Color.White)
            ) {

                Text(
                    text = "Choose account",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )

                LazyColumn{
                    items(list) {
                        tempAccount -> AccountText(account = tempAccount)
                    }
                }

                Row {
                    Button(
                        onClick = { onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Cancel")
                    }
                }


            }
        }
    }
}

@Composable
fun AccountText(account : Account) {

    val context = LocalContext.current

    Card(elevation = 2.dp) {
        Text(text = "Account ${account.id} | IBAN ${account.IBAN}" ,
            fontSize = 18.sp ,
            modifier = Modifier.padding(4.dp).padding(2.dp).clickable
            { Toast.makeText(context, "Hay", Toast.LENGTH_SHORT).show() })
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MBankingTheme {

    }
}