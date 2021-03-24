package com.madispace.core.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromList(value: List<String>?): String? {
        return value?.let { gson.toJson(value) }
    }

    @TypeConverter
    fun stringToList(date: String?): List<String> {
        return gson.fromJson(date, object : TypeToken<String>() {}.type)
    }
}