package com.yadagame.yadl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ButtonActivity extends Activity {

    public static boolean ACTIVE;
    private ActivityControlReceiver mActivityControlReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mActivityControlReceiver == null){
            mActivityControlReceiver = new ActivityControlReceiver();
        }
        IntentFilter intentFilter = new IntentFilter(ActivityWindow.TERMINATE);
        registerReceiver(mActivityControlReceiver, intentFilter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        ACTIVE = true;
    }

    @Override
    protected void onStop(){
        super.onStop();
        ACTIVE = false;
        unregisterReceiver(mActivityControlReceiver);
    }

    private class ActivityControlReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ActivityWindow.TERMINATE)) {
                finish();
            }
        }
    }
}
