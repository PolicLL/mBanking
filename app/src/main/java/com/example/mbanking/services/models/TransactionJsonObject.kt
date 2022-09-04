package com.example.mbanking.services.models

data class TransactionJsonObject(
    val id: Int,
    val date: String,
    val description: String,
    val amount: String,
    val type: String?){

    override fun toString(): String {
        return "TransactionJsonObject(id=$id, date='$date', description='$description'," +
                " amount='$amount', type=$type) \n"
    }
}