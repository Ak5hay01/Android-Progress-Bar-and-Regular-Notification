package com.AkshayTeli.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.lang.Thread.sleep


class MainActivity : AppCompatActivity() {

    private var primaryProgressStatus: Int =100
    private val notificationId = 10
    var jumpTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            showNotification()
        }

        var buttonProgessBar = findViewById<Button>(R.id.button2)
        buttonProgessBar.setOnClickListener {
            showProgressBarNotification()
        }

    }


    /*
    * Function used for showing progressbar notification
    * */
    private fun showProgressBarNotification() {

//        initializing of Notification builder
        val builder = NotificationCompat.Builder(applicationContext, "YOUR_CHANNEL_ID")
            .setContentTitle("Picture Download")
            .setContentText("Download in progress")
            .setSmallIcon(R.drawable.icon_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)

        val PROGRESS_MAX = 100
        var PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(this).apply {

            //Run dummy inputs to the Progressbar
            while (jumpTime < primaryProgressStatus) {

                    sleep(500);
                    jumpTime += 5;
                PROGRESS_CURRENT = jumpTime
                // When done, update the notification one more time to remove the progress bar
                builder.setContentText("Download in progress $PROGRESS_CURRENT %")
                    .setOngoing(true)
                    .setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                notify(notificationId, builder.build())


                if(jumpTime == primaryProgressStatus){

                    builder.setContentText("Download Completed")
                        .setProgress(0, 0, false)
                        .setOngoing(false)

                    notify(notificationId, builder.build())

                    Toast.makeText(applicationContext, "Done", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /*
    * Function used for creating the notification and if click on notification it will be redirected to pre-defined Activity
    * */
    private fun showNotification() {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "YOUR_CHANNEL_ID",
                "YOUR_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
            mNotificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, NotificationInformation::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Pending intent is used to start the aplication to specific page if we click on notification
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        //Used for creating Notification
        val mBuilder = NotificationCompat.Builder(applicationContext, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.drawable.icon_foreground) // notification icon
            .setContentTitle("Notification Title") // title for notification
            .setContentText("Notification Description")// message for notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // clear notification after click

        mNotificationManager.notify(0, mBuilder.build())
    }


}