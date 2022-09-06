package com.example.mbanking.ui.composables

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.mbanking.database.model.Account
import com.example.mbanking.database.model.Transaction
import com.example.mbanking.ui.navigation.AppScreenNames
import com.example.mbanking.viewModel.AccountViewModel
import java.time.LocalDate
import java.time.ZoneId


var TAG = "AccountScreen"

/*

Log.i(TAG , "Before if statement : $tempAccount \n")
Log.i(TAG , "Before if statement : $tempAccountTransactions \n")


 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AccountScreen(accountViewModel : AccountViewModel,
                  navController: NavController,
                  userId : Int?) {


    Log.i(TAG , "userId : $userId")

    if (userId != null) {
        accountViewModel.getData(userId)
    }

    var tempAccount = accountViewModel.tempAccount.collectAsState().value

    var tempAccountTransactions = accountViewModel.transactions.collectAsState().value

    var showDialog by remember { mutableStateOf(false) }

    Log.i("AccountScreen" , "After If tempAccount : $tempAccount")
    Log.i("AccountScreen" , "After If tempAccountTransactions : $tempAccountTransactions")

    Column(modifier = Modifier.fillMaxSize()) {

        if(showDialog){
            AccountDialog(
                onDismiss = {showDialog = false},
                accountViewModel = accountViewModel
            )
        }

        Row(modifier = Modifier.fillMaxWidth() ,
            horizontalArrangement = Arrangement.SpaceBetween) {

            Button(onClick = {

                Log.i("AccountScreen" , "Before logOut tempAccount : $tempAccount")
                Log.i("AccountScreen" , "Before logOut tempAccountTransactions : $tempAccountTransactions")

                accountViewModel.logOut()

                tempAccount = Account()
                tempAccountTransactions = emptyList()

                Log.i("AccountScreen" , "After logOut tempAccount : $tempAccount")
                Log.i("AccountScreen" , "After logOut tempAccountTransactions : $tempAccountTransactions")

               navController.navigate(AppScreenNames.LogInScreen.route){

               }


            },
                modifier = Modifier.padding(6.dp)) {
                Text(text = "Log out")
            }

            Text(text = "Account ${tempAccount.id}" ,
                fontSize = 22.sp ,
                modifier = Modifier.padding(8.dp))

            Button(onClick = {

            showDialog = true

            },
                modifier = Modifier.padding(6.dp)) {
                Text(text = "Change Account")
            }
        }



        Card(elevation = 2.dp ,
        modifier = Modifier.padding(12.dp)) {

            Column(modifier = Modifier.padding(4.dp)) {

                Column (modifier = Modifier.padding(4.dp)){

                    Text(text = "IBAN : ${tempAccount.IBAN}" , fontSize = 20.sp)
                    Text(text = "Amount : ${tempAccount.amount} ${tempAccount.currency}" , fontSize = 20.sp)

                }
            }

        }

        LazyColumn (modifier = Modifier.padding(16.dp)) {
            Log.i("AccountScreen" , "In LAZY COLUMN")
            items(tempAccountTransactions) {
                    transaction -> TransactionItem(transaction)
            }
        }

    }

}

@Composable
fun AccountDialog(onDismiss:() -> Unit, accountViewModel: AccountViewModel) {

    val listOfAccounts by accountViewModel.accounts.collectAsState()

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

                    items(listOfAccounts) {
                            tempAccount -> AccountText(account = tempAccount ,
                        accountViewModel,
                        onChooseAccount = {onDismiss()})
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
fun AccountText(account : Account ,
                accountViewModel: AccountViewModel ,
                onChooseAccount : () -> Unit) {

    val context = LocalContext.current

    Card(elevation = 2.dp) {
        Text(text = "Account ${account.id} | IBAN ${account.IBAN}" ,
            fontSize = 18.sp ,
            modifier = Modifier
                .padding(4.dp)
                .padding(2.dp)
                .clickable
                {
                    Toast
                        .makeText(context, "You switched Accounts", Toast.LENGTH_SHORT)
                        .show()

                    accountViewModel.setTempAccount(account)
                    onChooseAccount()
                })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionItem(transaction: Transaction) {

    Row(modifier = Modifier
        .padding(8.dp)
        .padding(8.dp) ,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {

        val date: LocalDate = transaction.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        Text(text = "$date" , fontSize = 20.sp , modifier = Modifier.width(140.dp))

        Spacer(Modifier.width(50.dp))

        Column() {
            Text(text = transaction.description, fontSize = 18.sp)
            Text(text = "${transaction.amount} ${transaction.currency}" , fontSize = 24.sp)
        }
    }
}
