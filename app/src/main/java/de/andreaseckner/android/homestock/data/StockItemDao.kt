package de.andreaseckner.android.homestock.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StockItemDao {

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<StockItem>>

    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<StockItem>

    @Insert
    suspend fun insert(item: StockItem)

    @Update
    suspend fun update(item: StockItem)

    @Delete
    suspend fun delete(item: StockItem)

}