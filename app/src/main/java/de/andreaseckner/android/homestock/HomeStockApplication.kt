package de.andreaseckner.android.homestock

import android.app.Application
import de.andreaseckner.android.homestock.data.StockItemRoomDatabase

class HomeStockApplication : Application() {
    val database: StockItemRoomDatabase by lazy { StockItemRoomDatabase.getDatabase(this) }
}