package com.example.mbanking.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account (
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    var amount : Double = 0.0,
    var IBAN: String = "123456789",
    var currency: String = "",
    var userId : Int = -1)
