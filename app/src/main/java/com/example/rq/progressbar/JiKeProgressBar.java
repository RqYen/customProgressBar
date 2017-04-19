package com.example.rq.progressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class JiKeProgressBar extends View {
    private static final float DEF_RATIO = 0.5f;
    private static final int DEF_REACHED_COLOR = Color.RED;
    private static final int DEF_UNREACHED_COLOR = Color.BLUE;
    private float mRatio;
    private int mReachedAreaColor;
    private int mUnreachedAreaColor;

    private RectF mReachedAreaRect;
    private Paint mReachedAreaPaint;

    private RectF mUnreachedAreaRect;
    private Paint mUnreachedAreaPaint;


    private RectF mProgressBarBorder;
    private Paint mProgressBarBorderPaint;

    private Bitmap mFlowerBitmap;
    private RectF mFlowerBitmapRect;
    private Integer mAnimatedValue;
    private boolean isJump;

    public JiKeProgressBar(Context context) {
        this(context,null);
    }

    public JiKeProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JiKeProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray =  getContext().obtainStyledAttributes(attrs, R.styleable.JiKeProgressBar);
        //进度条比例
        mRatio = typedArray.getFloat(R.styleable.JiKeProgressBar_jikeprogressbar_ratio,DEF_RATIO);
        //进度条到达区域颜色
        mReachedAreaColor = typedArray.getInt(R.styleable.JiKeProgressBar_jikeprogressbar_reached_area,DEF_REACHED_COLOR);
        //进度条未到达区域颜色
        mUnreachedAreaColor = typedArray.getInt(R.styleable.JiKeProgressBar_jikeprogressbar_unreached_area,DEF_UNREACHED_COLOR);


        mReachedAreaRect = new RectF();
        mReachedAreaPaint = new Paint();
        mReachedAreaPaint.setColor(mReachedAreaColor);

        mUnreachedAreaRect = new RectF();
        mUnreachedAreaPaint = new Paint();
        mUnreachedAreaPaint.setColor(mUnreachedAreaColor);

        mProgressBarBorder = new RectF();
        mProgressBarBorderPaint = new Paint();
        mProgressBarBorderPaint.setStyle(Paint.Style.STROKE);
        mProgressBarBorderPaint.setStrokeWidth(30);
        mProgressBarBorderPaint.setColor(Color.WHITE);

        mFlowerBitmapRect = new RectF();

        ValueAnimator animator = ValueAnimator.ofInt(1,100);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setRepeatCount(Integer.MAX_VALUE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isJump) {
                    mAnimatedValue = (Integer) animation.getAnimatedValue();
                    invalidate();
                }
            }
        });
        animator.start();
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(getSuggestedMinimumWidth(),widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                width = getSuggestedMinimumWidth();
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(getSuggestedMinimumHeight(),heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = getSuggestedMinimumWidth();
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 150;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 100;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mReachedAreaRect.set(0,0,getWidth() * mRatio,getHeight());
        mUnreachedAreaRect.set(getWidth() * mRatio,0,getWidth(),getHeight());
        mProgressBarBorder.set(0,0,getWidth(),getHeight());
        canvas.drawRect(mReachedAreaRect,mReachedAreaPaint);
        canvas.drawRect(mUnreachedAreaRect,mUnreachedAreaPaint);
        canvas.drawRoundRect(mProgressBarBorder,30,30,mProgressBarBorderPaint);

        if (mFlowerBitmap == null) {
            mFlowerBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.flower);
        }
        int flowerLeft = (int) (getWidth() * mRatio - mFlowerBitmap.getWidth() / 4);
        int flowerRight = (int) (getWidth() * mRatio + mFlowerBitmap.getWidth() / 4);
        if (mAnimatedValue != null) {
            mFlowerBitmapRect.set(flowerLeft, -mAnimatedValue, flowerRight, getHeight() - mAnimatedValue);
        } else {
            mFlowerBitmapRect.set(flowerLeft, 0, flowerRight, getHeight());

        }
        canvas.drawBitmap(mFlowerBitmap,null,mFlowerBitmapRect,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mReachedAreaPaint.setColor(mUnreachedAreaColor);
                mUnreachedAreaPaint.setColor(mReachedAreaColor);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mReachedAreaPaint.setColor(mReachedAreaColor);
                mUnreachedAreaPaint.setColor(mUnreachedAreaColor);
                invalidate();
                break;
        }

        return super.onTouchEvent(event);
    }

    public void setRatio(float ratio) {
        this.mRatio = ratio;
        invalidate();
    }
    public void setJump(boolean jump) {
        isJump = jump;
    }
}
