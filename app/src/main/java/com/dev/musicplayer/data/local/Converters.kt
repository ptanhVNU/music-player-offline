package com.dev.musicplayer.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromString(value: String): List<String>? {
        val listType: Type = object : TypeToken<ArrayList<String>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }


    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? =
        if (value != null) LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC)
        else null

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? =
        date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
}