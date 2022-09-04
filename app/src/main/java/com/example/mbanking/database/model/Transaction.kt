package com.example.mbanking.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) var id : Int = 0,

    var date: Date = Date(),
    var description: String = "",
    var amount: Double = 0.0,
    var currency: String = "",

    var type: String? = "",
    var accountId : Int = -1
){

    override fun toString(): String {
        return "Transaction(amount='$amount', date='$date', description='$description'," +
                " id='$id', type='$type') \n"
    }
}