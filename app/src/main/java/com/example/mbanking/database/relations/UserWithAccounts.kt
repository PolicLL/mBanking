package com.example.mbanking.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mbanking.database.model.Account
import com.example.mbanking.database.model.User

data class UserWithAccounts (
    @Embedded val user : User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val accounts : List<Account>)