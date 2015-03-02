package com.yadagame.yadl;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import wei.mark.standout.StandOutWindow;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StandOutWindow.closeAll(this, ActivityWindow.class);
        //StandOutWindow.show(this, ActivityWindow.class, StandOutWindow.DEFAULT_ID);

        NotificationCompat.Builder NotiBuilder = getYadlNotification();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, NotiBuilder.build());

        finish();
    }

    public NotificationCompat.Builder getYadlNotification(){

        NotificationCompat.Builder NotiBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.activity_icon)
                        .setColor(0xffF06916)
                        .setContentTitle("Yadl Running")
                        .setContentText("Tap to Open ADL Button");

        Intent ButtonIntent = new Intent(this, ButtonActivity.class);
        ButtonIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        ButtonIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotiBuilder.setContentIntent(notifyIntent);

        return NotiBuilder;
    }

}
