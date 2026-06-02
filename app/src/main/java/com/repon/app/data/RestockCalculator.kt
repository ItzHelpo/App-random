package com.repon.app.data

enum class RestockStatus { OK, SOON, OUT }

data class ItemStatus(
    val daysLeft: Int,
    val progress: Float,
    val status: RestockStatus
)

/** Pure functions deciding how soon an item must be repurchased. */
object RestockCalculator {

    fun statusFor(item: ConsumableItem, todayEpochDay: Long): ItemStatus {
        val runOut = item.startEpochDay + item.durationDays
        val daysLeft = (runOut - todayEpochDay).toInt()
        val elapsed = (todayEpochDay - item.startEpochDay).toFloat()
        val progress = if (item.durationDays <= 0) 1f else (elapsed / item.durationDays).coerceIn(0f, 1f)
        val status = when {
            daysLeft <= 0 -> RestockStatus.OUT
            daysLeft <= item.bufferDays -> RestockStatus.SOON
            else -> RestockStatus.OK
        }
        return ItemStatus(daysLeft, progress, status)
    }
}
