package com.yadagame.yadl;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RadioGroup;

import wei.mark.standout.StandOutWindow;

public class MainActivity extends Activity {

    private RadioGroup mModeGroup;
    private NotificationManager mNotificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(getClass().hashCode());
        StandOutWindow.closeAll(this, ActivityWindow.class);

        setContentView(R.layout.activity_logo);
        mModeGroup = (RadioGroup) findViewById(R.id.mode_select);

        mModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.noti){
                    NotificationCompat.Builder NotiBuilder = getYadlNotification();
                    mNotificationManager.notify(MainActivity.this.getClass().hashCode(), NotiBuilder.build());
                } else {
                    StandOutWindow.show(MainActivity.this, ActivityWindow.class, StandOutWindow.DEFAULT_ID);
                }

                finish();

            }
        });

    }

    public NotificationCompat.Builder getYadlNotification(){

        NotificationCompat.Builder NotiBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.activity_icon)
                        .setColor(0xff31A122)
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
