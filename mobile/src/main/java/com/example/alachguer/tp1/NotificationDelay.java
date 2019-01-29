package com.example.alachguer.tp1;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.app.RemoteInput;


public class NotificationDelay extends BroadcastReceiver {


    public static String  NOTIFICATION_ID = "notification-id";

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence("DELAY");
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        long delay;
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

//        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 1);
        Notification notification = null;

        StatusBarNotification[] barNotifications = notificationManager.getActiveNotifications();
        for(StatusBarNotification notif: barNotifications) {
            if (notif.getId() == id) {
                notification = notif.getNotification();
            }
        }

        String val = getMessageText(intent).toString();

        notificationManager.cancel(id);

        switch(val){
            case "1min":
                delay = 1000;
                break;
            case "5min":
                delay = 1000*5;
                break;
            case "30min":
                delay = 1000*30;
                break;
            default:
                delay = 1000;
        }



        try {
            //Délais de 10 secondes au choix
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        notificationManager.notify(id, notification);



    }
}