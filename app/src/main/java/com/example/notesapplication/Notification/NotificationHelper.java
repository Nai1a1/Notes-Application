package com.example.notesapplication.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class NotificationHelper {

    public static void scheduleNotification (Context context, long delayMillis, String content){

        Intent notificationIntent = new Intent(context, AlarmReceiver.class);
        notificationIntent.putExtra("content", content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            // Schedule the notification after the specified delay
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + delayMillis, pendingIntent);
        }
    }
}
