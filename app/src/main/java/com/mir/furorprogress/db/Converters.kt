package com.mir.furorprogress.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mir.furorprogress.models.Product

class Converters {

    @TypeConverter
    fun listToJson(value: List<Product>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Product>::class.java).toList()
}