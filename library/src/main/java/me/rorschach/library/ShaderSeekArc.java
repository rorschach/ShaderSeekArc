package me.rorschach.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lei on 16-2-8.
 */
public class ShaderSeekArc extends View {

    private float mStartValue = 16;
    private float mEndValue = 28;
    private float mProgress = 24;

    private float mStartAngle = -180;
    private float mEndAngle = 0;

    private boolean mIsShowMark = false;
    private boolean mIsShowProgress = true;

    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;

    private float mMinSize;
    private float mArcWidth;
    private float mRadius;
    private float mCenter;
    private float mInnerRadius;

    private RectF mArcRectF;

    private SweepGradient mSweepGradient;

    private int[] colors = {0xFF33B5E5, 0xFFFFBB33};

    private float[] positions = {0f, (mEndAngle - mStartAngle) / 306f};

    private OnSeekArcChangeListener mOnSeekArcChangeListener;

    public ShaderSeekArc(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShaderSeekArc(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShaderSeekArc(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (isInEditMode()) {
            return;
        }

        initAttr(context, attrs);
        initPaint();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShaderSeekArc);

            mStartValue = typedArray.getFloat(R.styleable.ShaderSeekArc_startValue, mStartValue);
            mEndValue = typedArray.getFloat(R.styleable.ShaderSeekArc_endValue, mEndValue);

            mStartAngle = typedArray.getFloat(R.styleable.ShaderSeekArc_startAngle, mStartAngle);
            mEndAngle = typedArray.getFloat(R.styleable.ShaderSeekArc_endAngle, mEndAngle);

            mProgress = typedArray.getFloat(R.styleable.ShaderSeekArc_progress, mProgress);
            mProgress = checkProgress(mProgress);

            mIsShowMark = typedArray.getBoolean(R.styleable.ShaderSeekArc_showMark, mIsShowMark);
            mIsShowProgress = typedArray.getBoolean(R.styleable.ShaderSeekArc_showProgress, mIsShowProgress);

        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
    }

    private void initPaint() {
        mArcPaint = new Paint();
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(0xffdddddd);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(0xff64646f);
    }

    private void initSize(int width, int height) {
        mMinSize = Math.min(width, height) / 600f;
        mArcWidth = 60 * mMinSize;
        mInnerRadius = 180 * mMinSize;
        mCenter = 300 * mMinSize;
        mRadius = 208 * mMinSize;

        mArcRectF = new RectF(
                mCenter - mRadius,
                mCenter - mRadius,
                mCenter + mRadius,
                mCenter + mRadius);


        mSweepGradient = new SweepGradient(mCenter, mCenter, colors, positions);
        Matrix matrix = new Matrix();
        matrix.preRotate(mStartAngle, mCenter, mCenter);
//        matrix.postRotate(mEndAngle, mCenter, mCenter);
        mSweepGradient.setLocalMatrix(matrix);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = 0;
        int height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //
            width = 600;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //
            height = 600;
        }

        initSize(width, height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArc(canvas);
        drawLine(canvas);
        drawProgress(canvas);
    }

    private void drawArc(Canvas canvas) {
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setShader(mSweepGradient);
        canvas.drawArc(
                mArcRectF,
                mStartAngle,
                getSweepAngle(mStartValue, mProgress),
                false,
                mArcPaint);

        mArcPaint.setShader(null);
        mArcPaint.setColor(Color.WHITE);
        canvas.drawArc(
                mArcRectF,
                mStartAngle + getSweepAngle(mStartValue, mProgress),
                getSweepAngle(mProgress, mEndValue),
                false, mArcPaint);
    }

    private float getSweepAngle(float from, float to) {
        return (to - from) / (mEndValue - mStartValue) * (mEndAngle - mStartAngle);
    }

    private void drawLine(Canvas canvas) {
        mLinePaint.setStrokeWidth(mMinSize * 3f);
        float rotate = mStartAngle + 90;
        canvas.rotate(rotate, mCenter, mCenter);
        mTextPaint.setTextSize(mMinSize * 30);

        float count = (mEndAngle - mStartAngle) / 3;

        for (int i = 0; i <= count; i++) {
            float top = mCenter - mInnerRadius - mArcWidth;
            if (i % 15 == 0) {
                top -= 20 * mMinSize;

                if (mIsShowMark) {
                    canvas.drawText(
                            (int) (Angle2Progress(i * 3 + mStartAngle)) + "",
                            mCenter - 15 * mMinSize,
                            top - 10 * mMinSize,
                            mTextPaint);
                }
            }
            canvas.drawLine(mCenter, mCenter - mInnerRadius,
                    mCenter, top,
                    mLinePaint);
            canvas.rotate(3, mCenter, mCenter);
            rotate += 3;
        }
        canvas.rotate(-rotate, mCenter, mCenter);
    }

    private void drawProgress(Canvas canvas) {
        mTextPaint.setTextSize(mMinSize * 30);
        if (mIsShowProgress) {
            canvas.drawText(String.format("%.1f", mProgress),
                    mCenter - 30 * mMinSize,
                    mCenter,
                    mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onStartTrackingTouch();
                break;
            case MotionEvent.ACTION_MOVE:

                float x = event.getX();
                float y = event.getY();

                if (checkTouch(x, y)) {

                    double angle = Math.toDegrees(Math.atan2((y - mCenter), (x - mCenter)));

                    if (!checkAngle(angle)) {

                        return false;
                    } else {
                        float progress = Angle2Progress(angle);
                        setProgress(progress);
                        return true;
                    }
                } else {
                    return false;
                }

            case MotionEvent.ACTION_UP:
                onStopTrackingTouch();
                setPressed(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                onStopTrackingTouch();
                setPressed(false);
                break;
        }

        return true;
    }

    private boolean checkTouch(float x, float y) {

        double distance = Math.sqrt((x - mCenter) * (x - mCenter)
                + (y - mCenter) * (y - mCenter));

        return distance >= mInnerRadius && distance <= (mInnerRadius + mRadius);
    }

    private boolean checkAngle(double angle) {
        if (mStartAngle <= -180) {
            return angle >= (mStartAngle + 360) || angle <= mEndAngle;
        } else {
            return angle >= mStartAngle & angle <= mEndAngle;
        }
    }

    public void setOnSeekArcChangeListener(OnSeekArcChangeListener l) {
        mOnSeekArcChangeListener = l;
    }

    private void onStartTrackingTouch() {
        if (mOnSeekArcChangeListener != null) {
            mOnSeekArcChangeListener.onStartTrackingTouch(this);
        }
    }

    private void onStopTrackingTouch() {
        if (mOnSeekArcChangeListener != null) {
            mOnSeekArcChangeListener.onStopTrackingTouch(this);
        }
    }

    private float Angle2Progress(double angle) {
        float progress;

        if (mStartAngle <= -180 && angle >= (mStartAngle + 360)) {
            progress = (float) (mStartValue + (angle - (mStartAngle + 360)) / (mEndAngle - mStartAngle) * (mEndValue - mStartValue));
        } else {
            progress = (float) (mEndValue - (mEndAngle - angle) / (mEndAngle - mStartAngle) * (mEndValue - mStartValue));
        }
        return progress;
    }

    public float getProgress() {
        checkProgress(mProgress);
        return mProgress;
    }

    public void setProgress(float progress) {
        checkProgress(progress);
        mProgress = progress;
        invalidate();
        if (mOnSeekArcChangeListener != null) {
            mOnSeekArcChangeListener.onProgressChanged(this, progress);
        }
    }

    private float checkProgress(float progress) {
        progress = progress > mEndValue ? mEndValue : progress;
        progress = progress < mStartValue ? mStartValue : progress;
        return progress;
    }

    public float getStartValue() {
        return mStartValue;
    }

    public void setStartValue(float startValue) {
        mStartValue = startValue;
    }

    public float getEndValue() {
        return mEndValue;
    }

    public void setEndValue(float endValue) {
        mEndValue = endValue;
    }

    public float getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(float startAngle) {
        mStartAngle = startAngle;
    }

    public float getEndAngle() {
        return mEndAngle;
    }

    public void setEndAngle(float endAngle) {
        mEndAngle = endAngle;
    }

    public boolean isShowMark() {
        return mIsShowMark;
    }

    public void setShowMark(boolean showMark) {
        mIsShowMark = showMark;
    }

    public boolean isShowProgress() {
        return mIsShowProgress;
    }

    public void setShowProgress(boolean showProgress) {
        mIsShowProgress = showProgress;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public float[] getPositions() {
        return positions;
    }

    public void setPositions(float[] positions) {
        this.positions = positions;
    }

    public interface OnSeekArcChangeListener {

        void onProgressChanged(ShaderSeekArc seekArc, float progress);

        void onStartTrackingTouch(ShaderSeekArc seekArc);

        void onStopTrackingTouch(ShaderSeekArc seekArc);
    }
}
