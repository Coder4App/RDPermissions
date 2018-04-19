package com.erongdu.wireless.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.erongdu.wireless.permissions.R;

import java.util.Arrays;
import java.util.List;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/4/19 下午2:08
 * <p>
 * Description: 侧边选择框
 * 仿https://github.com/Solartisan/WaveSideBar.git
 */
public class WaveSideBarView extends View {
    private static       String TAG     = "WaveSideBarView";
    // 计算波浪贝塞尔曲线的角弧长值
    private static final double ANGLE   = Math.PI * 45 / 180;
    private static final double ANGLE_R = Math.PI * 90 / 180;
    private int          mHeight;
    private int          mWidth;
    //画笔的高度
    private float        mItemHeight;
    //列表文字大小
    private int          mTextSize;
    // 渲染字母表
    private List<String> mLetters;
    //绘制文字的画笔
    private Paint mLetterPaint = new Paint();
    // 波浪画笔
    private Paint mWavePaint;
    //未被选中时文字的颜色
    private int   mTextNormalColor;
    //被选中时文字的颜色
    private int   mTextChoseColor;
    //波浪的颜色
    private int   mWaveColor;
    // 选中字体的坐标
    private float mPosX, mPosY;
    //当前选择的位置
    private int mChoose = -1;
    private int           oldChoose;
    //动画
    private ValueAnimator valueAnimator;
    // 波浪路径
    private Path mWavePath = new Path();
    // 圆形路径
    private Path mBallPath = new Path();
    // 手指滑动的Y点作为中心点
    private int   mCenterY; //中心点Y
    // 圆形中心点X
    private float mBallCentreX;
    // 用于绘制贝塞尔曲线的比率
    private float mRatio;
    // 贝塞尔曲线的分布半径
    private int   mRadius;
    // 圆形半径
    private int   mBallRadius;

    public WaveSideBarView(Context context) {
        this(context, null);
    }

    public WaveSideBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveSideBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WaveSideBarView);

        if (ta != null) {
            mLetters = Arrays.asList(ta.getResources().getStringArray(R.array.waveSideBarLetters));
            mTextChoseColor = ta.getColor(R.styleable.WaveSideBarView_sidebarTextChoseColor, Color.parseColor("#FFFFFF"));
            mTextNormalColor = ta.getColor(R.styleable.WaveSideBarView_sidebarTextNormalColor, Color.parseColor("#000000"));
            mTextSize = ta.getDimensionPixelSize(R.styleable.WaveSideBarView_sidebarTextSize, 24);
            mRadius = (int) ta.getDimension(R.styleable.WaveSideBarView_sidebarRadius, 20);
            mWaveColor = ta.getColor(R.styleable.WaveSideBarView_sidebarBackgroundColor, Color.parseColor("#be69be91"));
            mBallRadius = (int) ta.getDimension(R.styleable.WaveSideBarView_sidebarBallRadius, 50);
        }
        ta.recycle();

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setColor(mWaveColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();

        oldChoose = mChoose;
        mChoose = (int) Math.max(0, Math.floor(y / mItemHeight));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCenterY = (int) y;
                startAnimator(mRatio, 1.0f);
                break;
            case MotionEvent.ACTION_MOVE:
                mCenterY = (int) y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startAnimator(mRatio, 0f);
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mItemHeight = getMeasuredHeight() / (float) mLetters.size();
        mPosX = getMeasuredWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制文字列表
        drawLetters(canvas);
        //绘制波浪
        drawWavePath(canvas);
        //绘制圆球
        drawBallPath(canvas);
        //绘制圆球中的文字
        drawBallText(canvas);
    }

    /**
     * 绘制波浪
     *
     * @param canvas
     */
    private void drawWavePath(Canvas canvas) {
        mWavePath.reset();
        // 移动到起始点
        mWavePath.moveTo(mWidth, mCenterY - 3 * mRadius);
        //计算上部控制点的Y轴位置
        int controlTopY = mCenterY - 2 * mRadius;

        //计算上部结束点的坐标
        int endTopX = (int) (mWidth - mRadius * Math.cos(ANGLE) * mRatio);
        int endTopY = (int) (controlTopY + mRadius * Math.sin(ANGLE));
        mWavePath.quadTo(mWidth, controlTopY, endTopX, endTopY);

        //计算中心控制点的坐标
        int controlCenterX = (int) (mWidth - 1.8f * mRadius * Math.sin(ANGLE_R) * mRatio);
        int controlCenterY = mCenterY;
        //计算下部结束点的坐标
        int controlBottomY = mCenterY + 2 * mRadius;
        int endBottomX     = endTopX;
        int endBottomY     = (int) (controlBottomY - mRadius * Math.cos(ANGLE));
        mWavePath.quadTo(controlCenterX, controlCenterY, endBottomX, endBottomY);

        mWavePath.quadTo(mWidth, controlBottomY, mWidth, controlBottomY + mRadius);

        mWavePath.close();
        canvas.drawPath(mWavePath, mWavePaint);
    }

    /** 绘制字母列表 */
    private void drawLetters(Canvas canvas) {
        for (int i = 0; i < mLetters.size(); i++) {
            mLetterPaint.setColor(mTextNormalColor);
            mLetterPaint.setAntiAlias(true);
            mLetterPaint.setTextSize(mTextSize);
            mLetterPaint.setTextAlign(Paint.Align.CENTER);

            Paint.FontMetrics metrics    = mLetterPaint.getFontMetrics();
            float             textHeight = Math.abs(-metrics.top - metrics.bottom);
            mPosY = mItemHeight * i + mItemHeight / 2 + textHeight / 2;

            canvas.drawText(mLetters.get(i), mPosX, mPosY, mLetterPaint);
        }
    }

    /** 绘制圆球 */
    private void drawBallPath(Canvas canvas) {
        mBallCentreX = (mWidth + mBallRadius) - (2 * mBallRadius + (int) (1.8f * mRadius * Math.sin(ANGLE_R))) * mRatio;

        mBallPath.reset();
        mBallPath.addCircle(mBallCentreX, mCenterY, mBallRadius, Path.Direction.CCW);
        canvas.drawPath(mBallPath, mWavePaint);
    }

    /** 绘制圆球中的文字 */
    private void drawBallText(Canvas canvas) {
        if (mRatio > 0.8) {
            mLetterPaint.setColor(mTextNormalColor);
            mLetterPaint.setAntiAlias(true);
            mLetterPaint.setTextSize(mTextSize);
            mLetterPaint.setTextAlign(Paint.Align.CENTER);

            Paint.FontMetrics metrics    = mLetterPaint.getFontMetrics();
            float             textHeight = Math.abs(-metrics.top - metrics.bottom);

            canvas.drawText(mLetters.get(mChoose), mBallCentreX, mCenterY + textHeight / 2, mLetterPaint);
        }
    }

    private void startAnimator(float... value) {
        if (valueAnimator == null) {
            valueAnimator = new ValueAnimator();
        }

        valueAnimator.cancel();
        valueAnimator.setFloatValues(value);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRatio = (float) animation.getAnimatedValue();

                invalidate();
            }
        });

        valueAnimator.start();
    }
}
