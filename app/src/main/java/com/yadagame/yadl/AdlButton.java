package com.yadagame.yadl;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * Custom button for ADL Images
 */
public class AdlButton extends ViewGroup {

    private String mReference = ""; // Contain ADL Image URL
    private int mAdlDrawable;// Only for testing
    private int mRingColor = Color.GRAY;
    private float mRingWidth = 1.0f;

    private AdlImage mAdlImage;
    private Ring mRing;

    private float mLeftpadding;
    private float mRightpadding;
    private float mToppadding;
    private float mBottompadding;

    private Paint mRingPaint;
    private Paint mAdlImagePaint;

    private ObjectAnimator mLoadingAnimator;
    private final int LOADING_ANIMATE_TIME = 1000;

    public AdlButton(Context context) {
        super(context);
        init(null, 0);
    }

    public AdlButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AdlButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Do nothing.
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.AdlButton, defStyle, 0);

        try {
            //mReference= a.getString(R.styleable.AdlButton_reference);
            mRingColor = a.getColor(
                    R.styleable.AdlButton_ringColor,
                    Color.GRAY);
            mRingWidth = a.getDimension(R.styleable.AdlButton_ringWidth, 1.0f);

            if (a.hasValue(R.styleable.AdlButton_adlDrawable)) {
                mAdlDrawable = a.getResourceId(
                        R.styleable.AdlButton_adlDrawable, 0);
            }
        } finally {
            a.recycle();
        }

        mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.FILL);

        mAdlImagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAdlImagePaint.setAlpha(255);

        mRing = new Ring(getContext());
        addView(mRing);

        mAdlImage = new AdlImage(getContext());
        addView(mAdlImage);

        mLoadingAnimator = ObjectAnimator.ofInt(mRing, "angle", 0, 360);
        mLoadingAnimator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animator) {
                mRing.setVisible(true);
                //mAdlImage.setalpha(100);
            }

            public void onAnimationEnd(Animator animator) {
                mRing.setVisible(false);
                //mAdlImage.setalpha(255);
            }

            public void onAnimationCancel(Animator animator){
            }

            public void onAnimationRepeat(Animator animator) {
            }
        });
        //mLoadingAnimator.setIntValues(0, 360);
        mLoadingAnimator.setDuration(LOADING_ANIMATE_TIME);

        mGestureDetector = new GestureDetector(mAdlImage.getContext(), new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);

        return result;
    }

    private GestureDetector mGestureDetector;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e){
            if(!mLoadingAnimator.isRunning()){
                mLoadingAnimator.start();
            }
            return true;
        }

    }

    private class AdlImage extends View {
        private PointF mPivot = new PointF();
        private int mAlpha = 255;
        private Bitmap mCurrentAdlImage;
        private RectF mBound;

        public AdlImage(Context context) { super(context); }

        public int getalpha(){
            return mAlpha;
        }

        public void setalpha(int alp){
            mAlpha = alp;
            mAdlImagePaint.setAlpha(mAlpha);
            invalidate();
        }

        public void setPivot(float x, float y) {
            mPivot.x = x;
            mPivot.y = y;
            if (Build.VERSION.SDK_INT >= 11) {
                setPivotX(x);
                setPivotY(y);
            } else {
                invalidate();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Rect rect_src = new Rect(0, 0, mCurrentAdlImage.getWidth(), mCurrentAdlImage.getHeight());
            Paint bitmapPaint = new Paint();
            canvas.drawBitmap(mCurrentAdlImage, rect_src, mBound, bitmapPaint);
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mBound = new RectF(0, 0, w, h);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds = true;
            //BitmapFactory.decodeResource(getResources(), mAdlDrawable, options);

            //options.inSampleSize = calculateInSampleSize(options, w, h);
            options.inJustDecodeBounds = false;
            mCurrentAdlImage = getCroppedBitmap(BitmapFactory.decodeResource(getResources(), mAdlDrawable, options));
        }

        private int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {

            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        private Bitmap getCroppedBitmap(Bitmap bitmap) {

            final int color = 0xff424242;
            final Paint paint = new Paint();

            final int diameter = Math.min(bitmap.getWidth(), bitmap.getHeight());

            Bitmap output = Bitmap.createBitmap(diameter,
                    diameter, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            paint.setAntiAlias(true);
            Rect rect_src;
            if(bitmap.getWidth() > bitmap.getHeight()){
                rect_src = new Rect((bitmap.getWidth() - diameter)/2, 0, diameter, diameter);
            } else {
                rect_src = new Rect(0, (bitmap.getHeight() - diameter)/2, diameter, diameter);
            }

            final Rect rect_dst = new Rect(0, 0, diameter, diameter);

            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);

            canvas.drawCircle(diameter / 2, diameter / 2,
                    diameter / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect_src, rect_dst, paint);

            return output;
        }
    }

    private class Ring extends View {
        private PointF mPivot = new PointF();
        private boolean mVisable = false;
        private RectF mBound;
        private int mAngle = 0;

        public boolean getVisible(){
            return mVisable;
        }

        public void setVisible(boolean vis){
            mVisable = vis;
            invalidate();
        }

        public int getAngle(){
            return mAngle;
        }

        public void setAngle(int ang){
            mAngle = ang;
            invalidate();
        }

        public Ring(Context context) {
            super(context);
        }

        public void setPivot(float x, float y) {
            mPivot.x = x;
            mPivot.y = y;
            if (Build.VERSION.SDK_INT >= 11) {
                setPivotX(x);
                setPivotY(y);
            } else {
                invalidate();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if(mVisable) {
                canvas.drawArc(mBound, 270, this.mAngle, true, mRingPaint);
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mBound = new RectF(0, 0, w, h);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mLeftpadding = getPaddingLeft();
        mRightpadding = getPaddingRight();
        mToppadding = getPaddingTop();
        mBottompadding = getPaddingBottom();

        float xpad = (float) (mLeftpadding + mRightpadding);
        float ypad = (float) (mToppadding + mBottompadding);

        float legal_w = (float) w - xpad;
        float legal_h = (float) h - ypad;

        float center_x = mLeftpadding + legal_w/2;
        float center_y = mToppadding + legal_h/2;

        float diameter = Math.min(legal_w, legal_h);
        float diameter_img = diameter - 2*mRingWidth;

        mRing.setPivot(center_x, center_y);
        mRing.layout((int)(center_x - diameter/2),
                (int)(center_y - diameter/2),
                (int)(center_x + diameter/2),
                (int)(center_y + diameter/2));

        mAdlImage.setPivot(center_x, center_y);
        mAdlImage.layout((int)(center_x - diameter_img/2),
                (int)(center_y - diameter_img/2),
                (int)(center_x + diameter_img/2),
                (int)(center_y + diameter_img/2));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setreference(String ref) { mReference = ref; invalidate();}

    public String getreference() { return mReference; }

    public void setadlDrawable(int res){ mAdlDrawable = res; invalidate();}

    public int getadlDrawable(){ return mAdlDrawable; }

    public void setringColor(int col){ mRingColor = col; invalidate();}

    public int getringColor(){ return mRingColor; }

    public float getringWidth(){ return mRingWidth; }

    public void setringWidth(float rwidth) { mRingWidth = rwidth; invalidate();}

}
