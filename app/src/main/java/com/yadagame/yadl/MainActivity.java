package com.yadagame.yadl;

import android.app.Activity;
import android.os.Bundle;

import wei.mark.standout.StandOutWindow;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StandOutWindow.closeAll(this, ActivityWindow.class);
        StandOutWindow.show(this, ActivityWindow.class, StandOutWindow.DEFAULT_ID);

        finish();
    }

}
