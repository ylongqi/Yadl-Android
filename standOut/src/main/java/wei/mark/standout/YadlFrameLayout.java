package wei.mark.standout;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by ylongqi on 2/14/15.
 */
public class YadlFrameLayout extends FrameLayout {

    public static int init_x;
    public static int init_y;
    public static boolean init_flag = false;

    public YadlFrameLayout(Context context){
        super(context);
    }

    public YadlFrameLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @TargetApi(21)
    public YadlFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.i("onInterceptTouchEvent", Integer.toString(ev.getAction()));

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                init_flag = true;
                return true;
            }
            case MotionEvent.ACTION_DOWN: {
                init_x = (int) ev.getRawX();
                init_y = (int) ev.getRawY();
                init_flag = false;
                return false;
            }
        }

        return false;
    }
}
