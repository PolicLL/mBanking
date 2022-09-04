package com.example.mbanking.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.*

class DateConverter : Serializable {

    var gson = Gson()

    @TypeConverter
    fun stringToDate(data: String?): Date? {
        if (data == null) {
            return Date()
        }

        val listType = object : TypeToken<Date?>() {}.type

        return gson.fromJson<Date?>(data, listType)
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return gson.toJson(date)
    }

}