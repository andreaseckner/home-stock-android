package de.andreaseckner.android.homestock.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StockItem::class], version = 1, exportSchema = false)
abstract class StockItemRoomDatabase : RoomDatabase() {
    abstract fun stockItemDao(): StockItemDao

    companion object {

        @Volatile
        private var INSTANCE: StockItemRoomDatabase? = null

        fun getDatabase(context: Context): StockItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StockItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
