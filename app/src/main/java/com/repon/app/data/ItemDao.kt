package com.repon.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<ConsumableItem>>

    @Query("SELECT * FROM items")
    suspend fun getAllOnce(): List<ConsumableItem>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getById(id: Long): ConsumableItem?

    @Query("SELECT * FROM items WHERE id = :id")
    fun observeById(id: Long): Flow<ConsumableItem?>

    @Insert
    suspend fun insert(item: ConsumableItem): Long

    @Update
    suspend fun update(item: ConsumableItem)

    @Delete
    suspend fun delete(item: ConsumableItem)

    @Insert
    suspend fun insertLog(log: PurchaseLog)

    @Query("SELECT intervalDays FROM purchase_log WHERE itemId = :itemId ORDER BY id DESC LIMIT :limit")
    suspend fun recentIntervals(itemId: Long, limit: Int): List<Int>
}
