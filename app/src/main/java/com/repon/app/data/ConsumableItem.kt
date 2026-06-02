package com.repon.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** A consumable the user wants to be reminded to repurchase. */
@Entity(tableName = "items")
data class ConsumableItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val emoji: String = "📦",
    /** Current estimate of how many days one unit lasts. Self-adjusts over time. */
    val durationDays: Int,
    /** How many days before running out we should warn. */
    val bufferDays: Int = 3,
    /** Epoch-day when the current cycle started (last bought / opened). */
    val startEpochDay: Long,
    val notify: Boolean = true,
    /** Number of completed restock cycles used to refine [durationDays]. */
    val restockCount: Int = 0,
    /** Cycle start we last notified for, so we warn only once per cycle. */
    val lastNotifiedCycleStart: Long = -1L,
    val createdEpochDay: Long
)

/** One recorded repurchase interval, used to learn the real consumption pace. */
@Entity(
    tableName = "purchase_log",
    foreignKeys = [
        ForeignKey(
            entity = ConsumableItem::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("itemId")]
)
data class PurchaseLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: Long,
    val intervalDays: Int,
    val epochDay: Long
)
