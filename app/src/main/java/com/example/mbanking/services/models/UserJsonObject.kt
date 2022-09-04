package com.example.mbanking.services.models

import com.squareup.moshi.Json

data class UserJsonObject(
    @Json(name = "acounts")
    var accounts : List<AccountJsonObject>? = emptyList(),
    var user_id: Int = 0){

    override fun toString(): String {
        return "UserJsonObject(accounts=$accounts, user_id=$user_id)"
    }
}