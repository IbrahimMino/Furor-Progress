package com.mir.furorprogress.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.TypeConverters

import com.mir.furorprogress.models.Product
import com.mir.furorprogress.models.ProductEntity

@Database(entities = [Product::class,ProductEntity::class], version = 7)
@TypeConverters(Converters::class)
abstract class RoomDatabase: androidx.room.RoomDatabase(){

    abstract fun dao():Dao
    abstract fun daoEntity(): DaoEntity

    companion object {
        var INSTANCE: RoomDatabase? = null
        fun initDatabase(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabase::class.java,
                    "PrrefrgrefguctEn"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
        }
        fun getDatabase(): RoomDatabase {
            return INSTANCE!!
        }
    }
}