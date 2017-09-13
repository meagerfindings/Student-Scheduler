package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by matgreten on 9/12/17.
 */

public class AlertHandler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationID = intent.getExtras().getInt("notificationID");
        String notificationTitle = intent.getExtras().getString("notificationTitle");
        String notificationText = intent.getExtras().getString("notificationText");

        createNotification(context, notificationTitle, notificationText, "Reminder Alert", notificationID);
    }

    private void createNotification(Context context, String title, String message, String alert, int notificationID) {
        PendingIntent notificationIntent = PendingIntent.getActivity(context, notificationID, new Intent(context, MainActivity.class), 0);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setTicker(alert)
                        .setContentText(message)
                        .setSmallIcon(android.R.drawable.ic_popup_reminder);

        notificationBuilder.setContentIntent(notificationIntent);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }
}
