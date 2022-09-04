package com.example.mbanking.services

import com.example.mbanking.services.models.UserJsonObject
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL_L = "https://mportal.asseco-see.hr/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_L)
    .build()

interface ApiService{

    @GET("builds/ISBD_public/Zadatak_1.json")
    suspend fun getUser() : UserJsonObject

}

object Api{
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java)}
}