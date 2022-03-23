package com.example.quiz2_prep

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.Wickie.R

// https://developer.android.com/training/notify-user/build-notification
// https://www.youtube.com/watch?v=B5dgmvbrHgs
class NotificationUtils(currentActivity: Fragment) {

    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationID = 101
    private var currentActivity: Fragment = currentActivity

    /*
    * Function ensures notiication appears for android versions above O
    *
    * */
    fun createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            // Then craete notification Channel
            // Name of Notification inside Settings
            val name = "Notification Title"
            // Description in Notification inside Settings
            val descriptionText = "Notification Description"
            // Priority
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager : NotificationManager = currentActivity.requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("Notif Activity", "Channel Created ")
        }
    }
    /*
    * Builder that creates notification
    * */
    fun sendNotification(resources: Resources)
    {
        val intent = Intent (currentActivity.requireContext(),NotificationUtils::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent : PendingIntent = PendingIntent.getActivity(currentActivity.requireContext(), 0,intent, PendingIntent.FLAG_IMMUTABLE)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)
        val bitmaplargeIcon = BitmapFactory.decodeResource(resources, R.drawable.slimeballwickie)

        val builder = NotificationCompat.Builder(currentActivity.requireContext(),CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Example Title")
            .setContentText("Example Description")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(currentActivity.requireContext()))
        {
            notify(notificationID, builder.build())
        }
        Log.d("Notif Activity", "Notif Sent ")
    }

}