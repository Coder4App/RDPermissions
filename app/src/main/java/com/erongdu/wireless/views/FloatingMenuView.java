package com.erongdu.wireless.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
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
    private static final String TAG        = "FloatingMenuView";
    //弧度制时用到 PI = 180度
    private final        double PI         = Math.PI;
    //左边
    private static final int    POS_LEFT   = 0;
    //右边
    private static final int    POS_RIGHT  = 1;
    //顶部
    private static final int    POS_TOP    = 2;
    //底部
    private static final int    POS_BOTTOM = 3;
    //上一次横向的位置
    private int            lastHorizontalPosition;
    //当前横向位置
    private int            currentHorizontalPosition;
    //上一次的纵向位置
    private int            lastVerticalPosition;
    //当前纵向位置
    private int            currentVerticalPosition;
    //主按钮drawable
    private BitmapDrawable homeDrawable;
    //画笔
    private Paint mPaint = new Paint();
    //父控件的宽度
    private int             parentWidth;
    //父控件的高度
    private int             parentHeight;
    //控件的高度
    private int             mHeight;
    //控件的宽度
    private int             mWidth;
    //子菜单半径
    private int             itemRadius;
    //主按钮半径
    private int             homeDrawableRadius;
    //主菜单和子菜单之间的间距
    private float           offsetToHome;
    //主菜单的内边距
    private float           innerPadding;
    //主菜单绘制的边框
    private Rect            rectHome;
    //主菜单动画
    private ValueAnimator   homeAnimator;
    //item动画
    private ValueAnimator   itemAnimator;
    //手势监听类
    private GestureDetector gestureDetector;
    //是否显示菜单
    private boolean         showMenu;
    //用户获取坐标的工具类
    private PathMeasure     pathMeasure;
    //绘制路径时的path
    private Path            path;
    //item动画的比率
    private float           ratio;

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
            homeDrawable = (BitmapDrawable) ta.getDrawable(R.styleable.FloatingMenuView_FMhomeImageRes);
            offsetToHome = ta.getDimensionPixelOffset(R.styleable.FloatingMenuView_FMoffsetTohome, 40);
            itemRadius = ta.getDimensionPixelOffset(R.styleable.FloatingMenuView_FMitemRadius, 50);
            innerPadding = ta.getDimensionPixelOffset(R.styleable.FloatingMenuView_FMinnerPadding, 10);
        }
        ta.recycle();

        gestureDetector = new GestureDetector(context, gestureListener);
        rectHome = new Rect();
        pathMeasure = new PathMeasure();
        path = new Path();
        showMenu = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        parentWidth = ((ViewGroup) getParent()).getWidth();
        parentHeight = ((ViewGroup) getParent()).getHeight();
        currentHorizontalPosition = lastHorizontalPosition = getHorizontalPos();
        currentVerticalPosition = lastVerticalPosition = getVerticalPos();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = mWidth = getWidthAndHeight();
        setMeasuredDimension(mHeight, mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHomeDrawable(canvas);
        if (showMenu) {
            drawItem(canvas);
        }
    }

    //上一次点击的x轴
    private int lastPosX;
    //上一次点击的y轴
    private int lastPosY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //用于监听手势是否是点击还是滑动
        gestureDetector.onTouchEvent(ev);

        int currentPosX = (int) ev.getX();
        int currentPoxY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectHome.contains(currentPosX, currentPoxY)) {
                    lastPosX = currentPosX;
                    lastPosY = currentPoxY;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = currentPosX - lastPosX;
                int deltaY = currentPoxY - lastPosY;
                offsetLeftAndRight(deltaX);

                if ((getTop() + deltaY < 0 && deltaY < 0)) {
                    //防止滑出顶部
                    offsetTopAndBottom(-getTop());
                } else if ((getBottom() + deltaY > parentHeight && deltaY > 0)) {
                    //防止滑出底部
                    offsetTopAndBottom(parentHeight - getBottom());
                } else {
                    offsetTopAndBottom(deltaY);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                autoMoveToEdge();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    //*********************************/

    /**
     * 绘制主按钮
     */
    private void drawHomeDrawable(Canvas canvas) {
        int left, top, right, bottom;
        if (getVerticalPos() == POS_TOP && getHorizontalPos() == POS_LEFT) {
            //左上位置
            left = (int) innerPadding;
            top = (int) innerPadding;
            right = left + homeDrawable.getIntrinsicWidth();
            bottom = top + homeDrawable.getIntrinsicHeight();
        } else if (getVerticalPos() == POS_TOP && getHorizontalPos() == POS_RIGHT) {
            //右上位置
            left = (int) (mWidth - innerPadding - homeDrawable.getIntrinsicWidth());
            top = (int) innerPadding;
            right = left + homeDrawable.getIntrinsicWidth();
            bottom = top + homeDrawable.getIntrinsicHeight();
        } else if (getVerticalPos() == POS_BOTTOM && getHorizontalPos() == POS_RIGHT) {
            //右下位置
            left = (int) (mWidth - innerPadding - homeDrawable.getIntrinsicWidth());
            top = (int) (mHeight - innerPadding - homeDrawable.getIntrinsicHeight());
            right = left + homeDrawable.getIntrinsicWidth();
            bottom = top + homeDrawable.getIntrinsicHeight();
        } else {
            //左下位置
            left = (int) innerPadding;
            top = (int) (mHeight - innerPadding - homeDrawable.getIntrinsicHeight());
            right = left + homeDrawable.getIntrinsicWidth();
            bottom = top + homeDrawable.getIntrinsicHeight();
        }
        rectHome.set(left, top, right, bottom);
        canvas.drawBitmap(homeDrawable.getBitmap(), null, rectHome, mPaint);
    }

    //存贮 x,y坐标
    float[] pos = new float[2];

    /**
     * 绘制子菜单
     */
    private void drawItem(Canvas canvas) {
        //测试阶段用number暂时代替
        int number = 4;
        //弧度制
        double perAngle = (PI / 2 / number) / 2;
        //坐标位置
        float homePosX, homePosY, itemPosX, itemPosY;

        if (getVerticalPos() == POS_TOP && getHorizontalPos() == POS_LEFT) {
            //左上位置
            homePosX = innerPadding + homeDrawableRadius;
            homePosY = innerPadding + homeDrawableRadius;
            for (int i = 0; i < number; i++) {
                itemPosX = (float) ((mHeight - itemRadius) * Math.sin((2 * i + 1) * perAngle));
                itemPosY = (float) ((mWidth - itemRadius) * Math.cos((2 * i + 1) * perAngle));

                drawItems(canvas, homePosX, homePosY, itemPosX, itemPosY);
            }
        } else if (getVerticalPos() == POS_TOP && getHorizontalPos() == POS_RIGHT) {
            //右上位置
            homePosX = mWidth - (innerPadding + homeDrawableRadius);
            homePosY = innerPadding + homeDrawableRadius;
            for (int i = 0; i < number; i++) {
                itemPosX = (float) (mWidth - (mWidth - itemRadius) * Math.cos((2 * i + 1) * perAngle));
                itemPosY = (float) ((mHeight - itemRadius) * Math.sin((2 * i + 1) * perAngle));

                drawItems(canvas, homePosX, homePosY, itemPosX, itemPosY);
            }
        } else if (getVerticalPos() == POS_BOTTOM && getHorizontalPos() == POS_RIGHT) {
            //右下位置
            homePosX = mWidth - (innerPadding + homeDrawableRadius);
            homePosY = mHeight - (innerPadding + homeDrawableRadius);
            for (int i = 0; i < number; i++) {
                itemPosY = (float) (mHeight - (mWidth - itemRadius) * Math.cos((2 * i + 1) * perAngle));
                itemPosX = (float) (mWidth - (mHeight - itemRadius) * Math.sin((2 * i + 1) * perAngle));
                drawItems(canvas, homePosX, homePosY, itemPosX, itemPosY);
            }
        } else {
            //左下位置
            homePosX = innerPadding + homeDrawableRadius;
            homePosY = mHeight - innerPadding - homeDrawableRadius;

            for (int i = 0; i < number; i++) {
                itemPosX = (float) ((mWidth - itemRadius) * Math.cos((2 * i + 1) * perAngle));
                itemPosY = (float) (mHeight - (mHeight - itemRadius) * Math.sin((2 * i + 1) * perAngle));

                drawItems(canvas, homePosX, homePosY, itemPosX, itemPosY);
            }
        }
    }

    private void drawItems(Canvas canvas, float homePosX, float homePosY, float itemPosX, float itemPosY) {
        path.reset();
        path.moveTo(homePosX, homePosY);
        path.lineTo(itemPosX, itemPosY);
        path.close();

        pathMeasure.setPath(path, true);
        pathMeasure.getPosTan(pathMeasure.getLength() * ratio, pos, null);
        canvas.drawCircle(pos[0], pos[1], itemRadius, mPaint);

        Log.e(TAG, "ratio = " + ratio + " / x = " + pos[0] + " / y =" + pos[1] + " / length " + pathMeasure.getLength());
    }

    /**
     * 手势类型监听类
     */
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            showMenu = !showMenu;
            openOrCloseMenu(0, 1);
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    /**
     * 解析当前位置在屏幕的 left 还是 right
     */
    private void resolveHorizontalPos() {
        if (lastHorizontalPosition == POS_LEFT && (getRight() <= (parentWidth / 2 + getMeasuredWidth() / 2))) {
            currentHorizontalPosition = POS_LEFT;
        } else if (lastHorizontalPosition == POS_LEFT && (getRight() > (parentWidth / 2 + getMeasuredWidth() / 2))) {
            currentHorizontalPosition = POS_RIGHT;
        } else if (lastHorizontalPosition == POS_RIGHT && (getLeft() >= (parentWidth / 2 - getMeasuredWidth() / 2))) {
            currentHorizontalPosition = POS_RIGHT;
        } else if (lastHorizontalPosition == POS_RIGHT && (getLeft() < (parentWidth / 2 - getMeasuredWidth() / 2))) {
            currentHorizontalPosition = POS_LEFT;
        }
    }

    /**
     * 获取当前位置在屏幕横向方向是 left 还是right
     */
    private int getHorizontalPos() {
        if (getRight() > (parentWidth / 2 + mWidth / 2)) {
            return POS_RIGHT;
        } else {
            return POS_LEFT;
        }
    }

    /**
     * 获取当前位置在屏幕垂直方向是 top 还是bottom
     */
    private int getVerticalPos() {
        if (getBottom() >= (parentHeight / 2 + mHeight / 2)) {
            return POS_BOTTOM;
        } else {
            return POS_TOP;
        }
    }

    /**
     * 回到边缘的动画
     */
    private void autoMoveToEdge() {

        resolveHorizontalPos();

        if (homeAnimator == null) {
            homeAnimator = new ValueAnimator();
        } else {
            homeAnimator.removeAllUpdateListeners();
            homeAnimator.removeAllListeners();
        }

        float       distance;
        final float startPosValue;
        if (currentHorizontalPosition == POS_LEFT) {
            distance = getLeft();
            startPosValue = getLeft();
        } else {
            distance = parentWidth - getRight();
            startPosValue = getRight();
        }

        homeAnimator.setFloatValues(0, distance);
        homeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                if (currentHorizontalPosition == POS_LEFT) {
                    offsetLeftAndRight(-(int) (getLeft() - (startPosValue - value)));
                } else {
                    offsetLeftAndRight((int) ((startPosValue + value) - getRight()));
                }
            }
        });

        homeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentVerticalPosition = getVerticalPos();
                if ((lastVerticalPosition != currentVerticalPosition) || (lastHorizontalPosition != currentHorizontalPosition)) {
                    invalidate();
                }
                lastVerticalPosition = currentVerticalPosition;
                lastHorizontalPosition = currentHorizontalPosition;
            }
        });

        homeAnimator.start();
    }

    /**
     * 打开或关闭菜单
     */
    private void openOrCloseMenu(float... values) {
        mPaint.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#FF4081"));
        mPaint.setStrokeWidth(1);

        if (itemAnimator == null) {
            itemAnimator = new ValueAnimator();
        } else {
            itemAnimator.removeAllUpdateListeners();
        }

        itemAnimator.setFloatValues(values);
        itemAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ratio = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        itemAnimator.start();
    }

    /**
     * 获取控件的宽高
     */
    private int getWidthAndHeight() {
        //已左下角为基准 计算长宽
        homeDrawableRadius = Math.max(homeDrawable.getIntrinsicHeight(), homeDrawable.getIntrinsicWidth()) / 2;
        return (int) (Math.floor((innerPadding + homeDrawableRadius) / Math.sin(PI / 4) + homeDrawableRadius + offsetToHome + 2 * itemRadius));
    }
}
