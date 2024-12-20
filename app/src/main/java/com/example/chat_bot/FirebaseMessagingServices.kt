package com.example.chat_bot

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingServices:FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM token",token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            showNotification(it.title,it.body)
        }
        message.data.let {
            data ->
            val roomId = data["roomId"]
            val roomName = data["roomName"]
        }
    }
    @SuppressLint("MissingPermission")
    private fun showNotification(title:String?, body:String?){
        val channelId = "chat_notification"
        val channelName = "chat Notification"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH)
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
        val notificationBuilder = NotificationCompat.Builder(this,channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(0, notificationBuilder.build())

    }
}