package com.example.mbanking.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    var user_id: Int = 0,
    var firstName : String = "first name",
    var secondName : String = "second name",
    var pin : String = "",
) {


    fun clearValues() {
        firstName = ""
        secondName = ""
        pin = ""
    }

}
