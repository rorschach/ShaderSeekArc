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

    private float mStartAngle = -225;
    private float mEndAngle = 45;

    private boolean mIsShowMark = true;
    private boolean mIsShowProgress = true;

    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;

    private float mMinSize;
    private float mArcWidth;
    private float mArcRadius;
    private float mCenter;
    private float mInnerRadius;

    private RectF mArcRectF;

    private float mArcWidthRate = 1 / 3f;
    private static final int sArcTotalWidth = 240;

    private SweepGradient mSweepGradient;

    private int mStartColor = 0xFF33B5E5;
    private int mEndColor = 0xFFFFBB33;

    private int[] mColors = new int[]{mStartColor, mEndColor};

    private float[] mPositions = null;

    private int mMarkColor = 0xff64646f;
    private int mProgressTextColor = 0xff64646f;
    private int mLineColor = 0xffdddddd;

    private int mMarkSize = 30;
    private int mProgressTextSize = 35;

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
            checkValueSet();

            mStartAngle = typedArray.getFloat(R.styleable.ShaderSeekArc_startAngle, mStartAngle);
            mEndAngle = typedArray.getFloat(R.styleable.ShaderSeekArc_endAngle, mEndAngle);
            checkAngleSet();

            mProgress = typedArray.getFloat(R.styleable.ShaderSeekArc_progress, mProgress);
            mProgress = checkProgressSet(mProgress);

            mArcWidthRate = typedArray.getFloat(R.styleable.ShaderSeekArc_arcWidthRate, mArcWidthRate);
            checkArcWidthRate();

            mStartColor = typedArray.getInt(R.styleable.ShaderSeekArc_startColor, mStartColor);
            mEndColor = typedArray.getInt(R.styleable.ShaderSeekArc_endColor, mEndColor);

            mIsShowMark = typedArray.getBoolean(R.styleable.ShaderSeekArc_showMark, mIsShowMark);
            mIsShowProgress = typedArray.getBoolean(R.styleable.ShaderSeekArc_showProgress, mIsShowProgress);

            mMarkSize = typedArray.getInt(R.styleable.ShaderSeekArc_markSize, mMarkSize);
            mMarkColor = typedArray.getInt(R.styleable.ShaderSeekArc_markColor, mMarkColor);

            mProgressTextSize = typedArray.getInt(R.styleable.ShaderSeekArc_progressTextSize, mProgressTextSize);
            mProgressTextColor = typedArray.getInt(R.styleable.ShaderSeekArc_progressTextColor, mProgressTextColor);

            mLineColor = typedArray.getInt(R.styleable.ShaderSeekArc_lineColor, mLineColor);
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
        mLinePaint.setColor(mLineColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(0xff64646f);
    }

    private void initSize(int width, int height) {

        mMinSize = Math.min(width, height) / 600f;
        mCenter = 300 * mMinSize;
        mArcWidth = mArcWidthRate * sArcTotalWidth * mMinSize;
        mInnerRadius = (1 - mArcWidthRate) * sArcTotalWidth * mMinSize;
        mArcRadius = mInnerRadius + mArcWidth / 2;

        mArcRectF = new RectF(
                mCenter - mArcRadius,
                mCenter - mArcRadius,
                mCenter + mArcRadius,
                mCenter + mArcRadius);

        if (mPositions != null) {
            mSweepGradient = new SweepGradient(mCenter, mCenter, mColors, mPositions);
        } else {
            mSweepGradient = new SweepGradient(mCenter, mCenter, mColors, null);
        }

        Matrix matrix = new Matrix();
        matrix.preRotate(mStartAngle, mCenter, mCenter);
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
            width = 600;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = 600;
        }

        initSize(width, height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArc(canvas);
        drawLine(canvas);
        drawText(canvas);
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

        mTextPaint.setTextSize(mMinSize * mMarkSize);
        mTextPaint.setColor(mMarkColor);

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
            canvas.drawLine(mCenter, mCenter - mInnerRadius + 0.5f,
                    mCenter, top,
                    mLinePaint);
            canvas.rotate(3, mCenter, mCenter);
            rotate += 3;
        }
        canvas.rotate(-rotate, mCenter, mCenter);
    }

    private void drawText(Canvas canvas) {
        mTextPaint.setTextSize(mMinSize * mProgressTextSize);
        mTextPaint.setColor(mProgressTextColor);

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

        return distance >= mInnerRadius && distance <= (mInnerRadius + mArcRadius);
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
        checkProgressSet(mProgress);
        return mProgress;
    }

    public void setProgress(float progress) {
        checkProgressSet(progress);
        mProgress = progress;
        invalidate();
        if (mOnSeekArcChangeListener != null) {
            mOnSeekArcChangeListener.onProgressChanged(this, progress);
        }
    }

    private float checkProgressSet(float progress) {
        if (progress >= mStartValue && progress <= mEndValue) {
            return progress;
        } else {
            throw new IllegalArgumentException("Progress is out of range!");
        }
    }

    public float getStartValue() {
        return mStartValue;
    }

    public void setStartValue(float startValue) {
        mStartValue = startValue;
        checkValueSet();
    }

    public float getEndValue() {
        return mEndValue;
    }

    public void setEndValue(float endValue) {
        mEndValue = endValue;
        checkValueSet();
    }

    private void checkValueSet() {
        if (mEndValue <= mStartValue) {
            throw new IllegalArgumentException("End value should large than the start value!");
        }
    }

    public float getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(float startAngle) {
        mStartAngle = startAngle;
        checkAngleSet();
    }

    public float getEndAngle() {
        return mEndAngle;
    }

    public void setEndAngle(float endAngle) {
        mEndAngle = endAngle;
        checkAngleSet();
    }

    private void checkAngleSet() {
        if (mEndAngle <= mStartAngle) {
            throw new IllegalArgumentException("End angle should large than the start angle!");
        } else if (mEndAngle - mStartAngle > 360) {
            throw new IllegalArgumentException("Arc angle shall not exceed 360!");
        }
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
        return mColors;
    }

    public void setColors(int[] colors) {
        this.mColors = colors;
    }

    public float[] getPositions() {
        return mPositions;
    }

    public void setPositions(float[] positions) {
        this.mPositions = positions;
    }

    public float getArcWidthRate() {
        checkArcWidthRate();
        return mArcWidthRate;
    }

    public void setArcWidthRate(float arcWidthRate) {
        checkArcWidthRate();
        mArcWidthRate = arcWidthRate;
    }

    private void checkArcWidthRate() {
        mArcWidthRate = Math.min(mArcWidthRate, 0.7f);
        mArcWidthRate = Math.max(0.3f, mArcWidthRate);
    }

    public int getStartColor() {
        return mStartColor;
    }

    public void setStartColor(int startColor) {
        mStartColor = startColor;
        mColors[0] = mStartColor;
    }

    public int getEndColor() {
        return mEndColor;
    }

    public void setEndColor(int endColor) {
        mEndColor = endColor;
        mColors[mColors.length - 1] = mEndColor;
    }

    public int getMarkColor() {
        return mMarkColor;
    }

    public void setMarkColor(int markColor) {
        mMarkColor = markColor;
    }

    public int getProgressTextColor() {
        return mProgressTextColor;
    }

    public void setProgressTextColor(int progressTextColor) {
        mProgressTextColor = progressTextColor;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
    }

    public int getMarkSize() {
        return mMarkSize;
    }

    public void setMarkSize(int markSize) {
        mMarkSize = markSize;
    }

    public int getProgressTextSize() {
        return mProgressTextSize;
    }

    public void setProgressTextSize(int progressTextSize) {
        mProgressTextSize = progressTextSize;
    }

    public interface OnSeekArcChangeListener {

        void onProgressChanged(ShaderSeekArc seekArc, float progress);

        void onStartTrackingTouch(ShaderSeekArc seekArc);

        void onStopTrackingTouch(ShaderSeekArc seekArc);
    }
}
