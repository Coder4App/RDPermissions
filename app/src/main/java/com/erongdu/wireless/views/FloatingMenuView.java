package com.erongdu.wireless.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.erongdu.wireless.permissions.R;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/4/21 上午10:29
 * <p>
 * Description:
 */
public class FloatingMenuView extends FrameLayout {
    private static final String TAG          = "FloatingMenuView";
    //左边
    private static final int    POS_LEFT     = 0;
    //右边
    private static final int    POS_RIGHT    = 1;
    //上一次的位置
    private              int    lastPosition = POS_LEFT;
    //当前位置
    private int              currentPosition;
    //主菜单drawable
    private BitmapDrawable   menuDrawable;
    //子菜单drawable
    private BitmapDrawable[] itemDrawableList;
    //主菜单和子菜单之间的间距
    private float            menuOffset;
    private Paint mPaint = new Paint();
    //父控件的宽度
    private int           parentWidth;
    //父控件的高度
    private int           parentHeight;
    //控件的高度
    private int           mHeight;
    //控件的宽度
    private int           mWidth;
    //主菜单绘制的边框
    private Rect          rectMenu;
    //主菜单动画
    private ValueAnimator valueAnimator;

    public FloatingMenuView(Context context) {
        this(context, null);
    }

    public FloatingMenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FloatingMenuView);
        if (ta != null) {

            menuDrawable = (BitmapDrawable) ta.getDrawable(R.styleable.FloatingMenuView_FMmenuImageRes);
            menuOffset = ta.getDimensionPixelOffset(R.styleable.FloatingMenuView_FMmenuOffset, 20);
            getItemResArray(ta.getResourceId(R.styleable.FloatingMenuView_FMitemImageRes, -1));
        }
        ta.recycle();

        rectMenu = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mHeight = measureHeight();
        mWidth = measureWidth();
        parentWidth = ((ViewGroup) getParent()).getWidth();
        parentHeight = ((ViewGroup) getParent()).getHeight();
        setMeasuredDimension(mHeight, mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectMenu.set(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + menuDrawable.getIntrinsicWidth(), getPaddingTop() + menuDrawable
                .getIntrinsicHeight());
        canvas.drawBitmap(menuDrawable.getBitmap(), null, rectMenu, mPaint);
        //        canvas.drawBitmap(menuDrawable.getBitmap(), getPaddingLeft(), getPaddingTop(), mPaint);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate" + getTop());
    }

    private int lastPosX;
    private int lastPosY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int currentPosX = (int) ev.getX();
        int currentPoxY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onInterceptTouchEvent ACTION_DOWN");
                if (rectMenu.contains(currentPosX, currentPoxY)) {
                    lastPosX = currentPosX;
                    lastPosY = currentPoxY;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = currentPosX - lastPosX;
                int deltaY = currentPoxY - lastPosY;
                offsetLeftAndRight(deltaX);

                Log.i(TAG, "onInterceptTouchEvent ACTION_MOVE " + getTop() + "／" + getBottom()+"////deltaY"+deltaY);
                if ((getTop() + deltaY < 0 && deltaY < 0)) {
                    offsetTopAndBottom(-getTop());
                } else if ((getBottom() + deltaY > parentHeight && deltaY >0)) {
                    offsetTopAndBottom(parentHeight - getBottom());
                } else {
                    offsetTopAndBottom(deltaY);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onInterceptTouchEvent ACTION_UP");
                resolvePos();
                autoMoveBack();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    //*********************************/

    /**
     * 解析当前位置在屏幕的 left 还是 right
     * left：
     * right：
     */
    private void resolvePos() {
        if (lastPosition == POS_LEFT && (getRight() <= (parentWidth / 2 + getMeasuredWidth() / 2))) {
            currentPosition = POS_LEFT;
            lastPosition = POS_LEFT;
        } else if (lastPosition == POS_LEFT && (getRight() > (parentWidth / 2 + getMeasuredWidth() / 2))) {
            currentPosition = POS_RIGHT;
            lastPosition = POS_RIGHT;
        } else if (lastPosition == POS_RIGHT && (getLeft() >= (parentWidth / 2 - getMeasuredWidth() / 2))) {
            currentPosition = POS_RIGHT;
            lastPosition = POS_RIGHT;
        } else if (lastPosition == POS_RIGHT && (getLeft() < (parentWidth / 2 - getMeasuredWidth() / 2))) {
            currentPosition = POS_LEFT;
            lastPosition = POS_LEFT;
        }
    }

    /**
     * 回到边缘的动画
     */
    private void autoMoveBack() {
        if (valueAnimator == null) {
            valueAnimator = new ValueAnimator();
        }

        float       distance = 0;
        final float startPosValue;
        if (currentPosition == POS_LEFT) {
            distance = getLeft();
            startPosValue = getLeft();
        } else {
            distance = parentWidth - getRight();
            startPosValue = getRight();
        }

        valueAnimator.setFloatValues(0, distance);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                if (currentPosition == POS_LEFT) {
                    offsetLeftAndRight(-(int) (getLeft() - (startPosValue - value)));
                } else {
                    offsetLeftAndRight((int) ((startPosValue + value) - getRight()));
                }
            }
        });
        valueAnimator.start();
    }

    /**
     * 测量控件的高度
     */
    private int measureHeight() {
        int paddingVertical = Math.max(getPaddingBottom(), getPaddingTop());
        return menuDrawable.getIntrinsicHeight() + itemDrawableList[0].getIntrinsicHeight() + paddingVertical;
    }

    /**
     * 测量控件的宽度
     */
    private int measureWidth() {
        int paddingHorizontal = Math.max(getPaddingLeft(), getPaddingRight());
        return menuDrawable.getIntrinsicWidth() + itemDrawableList[0].getIntrinsicWidth() + paddingHorizontal;
    }

    /**
     * 获取所有item
     */
    private void getItemResArray(int resID) {
        if (resID == -1) {
            return;
        }
        TypedArray resArrayID = getResources().obtainTypedArray(resID);
        itemDrawableList = new BitmapDrawable[resArrayID.length()];
        for (int i = 0; i < resArrayID.length(); i++) {
            itemDrawableList[0] = (BitmapDrawable) getResources().getDrawable(resArrayID.getResourceId(i, 0));
        }
        resArrayID.recycle();
    }
}
