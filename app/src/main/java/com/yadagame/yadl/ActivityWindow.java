package com.yadagame.yadl;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import wei.mark.standout.*;
import wei.mark.standout.ui.*;
import wei.mark.standout.constants.*;

/**
 * Created by ylongqi on 2/13/15.
 */
public class ActivityWindow extends StandOutWindow {

    @Override
    public String getAppName() {
        return "Yadl";
    }

    @Override
    public int getAppIcon() {
        return R.drawable.activity;
    }

    @Override
    public void createAndAttachView(int id, FrameLayout frame){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_button, frame, true);
        ImageButton button = (ImageButton) view.findViewById(R.id.activity_img);


        button.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent act_intent = new Intent(ActivityWindow.this, ButtonActivity.class);
                    act_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(act_intent);
                }
            });

    }

    @Override
    public StandOutLayoutParams getParams(int id, Window window){
        return new StandOutLayoutParams(id, 150, 150, StandOutLayoutParams.RIGHT, StandOutLayoutParams.CENTER);
    }

    @Override
    public int getFlags(int id){
        return super.getFlags(id) | StandOutFlags.FLAG_BODY_MOVE_ENABLE | StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE;
    }

    @Override
    public String getPersistentNotificationMessage(int id){
        return "Click to close activity button";
    }

    @Override
    public Intent getPersistentNotificationIntent(int id){
        return StandOutWindow.getCloseIntent(this, ActivityWindow.class, id);
    }
}
