package com.repon.app.data

import kotlinx.coroutines.flow.Flow
import kotlin.math.roundToInt

class ItemRepository(private val dao: ItemDao) {

    val items: Flow<List<ConsumableItem>> = dao.observeAll()

    fun observe(id: Long): Flow<ConsumableItem?> = dao.observeById(id)

    suspend fun get(id: Long): ConsumableItem? = dao.getById(id)

    suspend fun upsert(item: ConsumableItem): Long =
        if (item.id == 0L) dao.insert(item) else {
            dao.update(item)
            item.id
        }

    suspend fun delete(item: ConsumableItem) = dao.delete(item)

    /**
     * The user bought more. Start a new cycle and refine [ConsumableItem.durationDays]
     * from the real interval, so the estimate gets smarter every time.
     */
    suspend fun markRestocked(item: ConsumableItem, todayEpochDay: Long) {
        if (item.id == 0L) return
        val interval = (todayEpochDay - item.startEpochDay).toInt()
        var newDuration = item.durationDays
        var newCount = item.restockCount
        if (interval in 1..3650) {
            dao.insertLog(PurchaseLog(itemId = item.id, intervalDays = interval, epochDay = todayEpochDay))
            val recents = dao.recentIntervals(item.id, 5)
            if (recents.isNotEmpty()) {
                newDuration = recents.average().roundToInt().coerceAtLeast(1)
            }
            newCount = item.restockCount + 1
        }
        dao.update(
            item.copy(
                startEpochDay = todayEpochDay,
                durationDays = newDuration,
                restockCount = newCount,
                lastNotifiedCycleStart = -1L
            )
        )
    }
}
