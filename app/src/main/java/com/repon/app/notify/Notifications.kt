package com.repon.app.notify

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.repon.app.MainActivity
import com.repon.app.R
import com.repon.app.data.ConsumableItem
import com.repon.app.data.RestockStatus

object Notifications {

    private const val CHANNEL_ID = "restock"

    fun ensureChannel(context: Context) {
        val channel = NotificationChannelCompat.Builder(
            CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(context.getString(R.string.channel_name))
            .setDescription(context.getString(R.string.channel_desc))
            .build()
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    fun notifyRestock(context: Context, item: ConsumableItem, status: RestockStatus) {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return

        val title = context.getString(
            if (status == RestockStatus.OUT) R.string.notif_title_out else R.string.notif_title_soon
        )
        val text = context.getString(
            if (status == RestockStatus.OUT) R.string.notif_text_out else R.string.notif_text_soon,
            "${item.emoji} ${item.name}".trim()
        )

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pending = PendingIntent.getActivity(
            context,
            item.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_repon)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setContentIntent(pending)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            manager.notify(item.id.toInt(), notification)
        } catch (_: SecurityException) {
            // Permission revoked between the check and the post; ignore.
        }
    }
}
