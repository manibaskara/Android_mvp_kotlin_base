@file:Suppress("UNUSED_VARIABLE", "SameParameterValue")

package com.magnum.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.common.reflect.TypeToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.magnum.app.R
import com.magnum.app.common.Constants
import com.magnum.app.view.activity.HomeActivity
import com.sendbird.android.shadow.com.google.gson.Gson
import timber.log.Timber

class FirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        Timber.d("Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.let { message ->
            createNotification(message)
        }
    }

    private fun createNotification(remoteMessage: RemoteMessage) {
        Timber.d("remoteMessage $remoteMessage")

        var title: String? = ""
        var body: String? = ""
        var clickAction = ""

        remoteMessage.data?.let {
            for (key in it.keys) {
                val value = it[key]
                Timber.d("Notification========>$key, $value, ${value?.javaClass?.name}")
            }
        }
        when {
            remoteMessage.data["data"] != null -> {
                Timber.d("remoteMessage data ${remoteMessage.data["data"]}")

                val data = Gson().fromJson<FcmNotificationData>(
                    remoteMessage.data["data"],
                    object : TypeToken<FcmNotificationData>() {}.type
                )
            }
            else -> remoteMessage.notification?.let {
                title = it.title
                body = it.body
                it.clickAction?.let { action ->
                    clickAction = action
                    Timber.d("remoteMessage clickAction $clickAction")
                }
            }
        }

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("skinella_customer", "General")
            } else {
                ""
            }

        val resultIntent = createIntentFromClickAction(clickAction)
        resultIntent.putExtra("NOTIFICATION", true)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT)
        }
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_skinella_transparent)
            .setColor(Color.WHITE)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setChannelId(channelId)
            .setContentIntent(resultPendingIntent)
            .setWhen(System.currentTimeMillis())
        val notification = builder.build()

        val manager = NotificationManagerCompat.from(applicationContext)
        manager.notify(0, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun createIntentFromClickAction(clickAction: String): Intent {
        val notificationType = Constants.NotificationTypeString
        val resultIntent: Intent
        resultIntent = when (clickAction) {
            notificationType.APPLIED -> {
                Intent(this, HomeActivity::class.java)
            }
            else -> {
                Intent(this, HomeActivity::class.java)
            }
        }
        return resultIntent
    }
}

data class FcmNotificationData(
    val body: String,
    val title: String
)