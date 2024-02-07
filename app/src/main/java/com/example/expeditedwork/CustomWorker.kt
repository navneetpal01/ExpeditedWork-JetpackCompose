package com.example.expeditedwork

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class CustomWorker constructor(
    context : Context,
    workParameters : WorkerParameters
) : CoroutineWorker(
    context,workParameters
) {
    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        delay(20000)
        Log.d("CustomWorker","Success!")
        return Result.Success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInfo(applicationContext)
    }
}


private fun getForegroundInfo(context: Context) : ForegroundInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ForegroundInfo(
            1,
            //Notification will not be visible on the higher android devices like android 13
            //as we have to ask notification permission in the android 13+
            createNotification(context = context),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
        )
    } else {
        ForegroundInfo(
            1,
            createNotification(context = context)
        )
    }
}


private fun createNotification(context : Context) : Notification {
    val channelId = "main_channel_id"
    val channelName = "Main Channel"

    val builder = NotificationCompat.Builder(context,channelId)
        .setSmallIcon(R.drawable.notification)
        .setContentTitle("Notification Title")
        .setContentText("This is my first Notification")
        .setOngoing(true)
        .setAutoCancel(true)
    //Okay so the Difference between the NotificationManagerClass and the NotificationManagerCompatClass is that the
    //new NotificationManager Class automatically manages the Notification according to the Android Version
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
    return builder.build()

}