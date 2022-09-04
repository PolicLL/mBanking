package com.example.mbanking.services.models

data class AccountJsonObject(
    val id: Int,
    val IBAN: String,
    val amount: String,
    val currency: String,
    val transactions: List<TransactionJsonObject>?){

    override fun toString(): String {
        return "AccountJsonObject(id=$id, IBAN='$IBAN', amount='$amount', " +
                "currency='$currency', transactions=$transactions) \n"
    }
}