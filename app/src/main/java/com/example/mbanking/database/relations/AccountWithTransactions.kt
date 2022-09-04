package com.example.mbanking.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mbanking.database.model.Account
import com.example.mbanking.database.model.Transaction

class AccountWithTransactions (
    @Embedded val account : Account,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId"
    )
    val transactions : List<Transaction>)
