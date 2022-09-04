package com.example.mbanking.viewModel.converters

class AmountAndCurrencyConverter {

    fun splitIntoAmountAndCurrency(amount : String) : TransactionValues{

        var value = amount.split(" ")

        var transactionValue = TransactionValues()

        transactionValue.amount = value[0].replace("," , ".").toDouble()
        transactionValue.currency = value[1]

        return transactionValue
    }

    /**
    This function convert amount from one format to another
     Example : "2.523,00" -> "2523.00"
     It removes commas and puts dots in right place so you can convert this string to double
     */
    fun convertAccountsAmount(amount : String) : Double {

        var test = amount

        test = test.replace("." , "").replace("," , ".")

        return test.toDouble()

    }

    data class TransactionValues(var amount : Double = 0.0 , var currency : String = "")

}