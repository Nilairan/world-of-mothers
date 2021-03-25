package com.madispace.core.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {

    @TypeConverter
    fun fromList(value: List<String>?): String? {
        val gson = Gson()
        return value?.let { gson.toJson(value) }
    }

    @TypeConverter
    fun stringToList(date: String?): List<String> {
        val gson = Gson()
        return gson.fromJson(date, object : TypeToken<List<String>>() {}.type)
    }
}