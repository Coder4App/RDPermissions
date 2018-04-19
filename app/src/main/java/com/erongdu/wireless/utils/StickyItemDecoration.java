package com.erongdu.wireless.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.erongdu.wireless.permissions.R;

/**
 * Author: chenwei
 * E-mail: cw@erongdu.com
 * Date: 2018/4/18 上午11:18
 * <p>
 * Description:
 */
public class StickyItemDecoration extends RecyclerView.ItemDecoration {
    private static String TAG_HOVERITEMDECORATION = "StickyItemDecoration";
    private        int    mHeight                 = 40;
    private Paint         mPaint;
    private Paint mTextPaint;
    public  GroupListener listener;

    public StickyItemDecoration() {
        mPaint = new Paint();
        mTextPaint = new Paint();
        mPaint.setColor(ActivityManage.peek().getResources().getColor(R.color.colorAccent));
        mTextPaint.setColor(ActivityManage.peek().getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //        Log.d("TAG_HOVERITEMDECORATION", "onDraw" + parent.getChildCount());
        //        int childCount = parent.getChildCount();
        //        for (int i = 0; i < childCount; i++) {
        //            View child  = parent.getChildAt(i);
        //            int  left   = parent.getLeft();
        //            int  right  = parent.getRight();
        //            int  top    = child.getBottom();
        //            int  bottom = top + mHeight;
        //            c.drawRect(left, top, right, bottom, mPaint);
        //        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int    childCount       = parent.getChildCount();
        int    left             = parent.getLeft() + parent.getPaddingLeft();
        int    right            = parent.getRight() - parent.getPaddingRight();
        String currentGroupName = null;
        String perGroupName;
        for (int i = 0; i < childCount; i++) {
            View child    = parent.getChildAt(i);
            int  position = parent.getChildAdapterPosition(child);
            perGroupName = currentGroupName;
            currentGroupName = listener.getGroupName(position);

            //都一个item的perGroupName始终为空所以第一个group永远悬停
            if (currentGroupName == null || (perGroupName != null && currentGroupName.endsWith(perGroupName))) {
                continue;
            }
            //开始绘制悬浮框
            int bottom = Math.max(mHeight, child.getTop());

            //下一个group开始接近第一个group(悬停的group)
            if (position + 1 <= childCount) {
                int childBottom = child.getBottom();
                String nextGroup = listener.getGroupName(position + 1);
                if (!nextGroup.endsWith(currentGroupName) &&  childBottom < bottom){
                    bottom = childBottom;
                }
            }

            c.drawRect(left, bottom - mHeight, right, bottom, mPaint);
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            //文字竖直居中显示
            float baseLine = bottom - (mHeight - (fm.bottom - fm.top)) / 2 - fm.bottom;
            c.drawText(currentGroupName, left + 5, baseLine, mTextPaint);
        }
    }

    /**
     * 设置 left right 相当于设置item的 paddingLeft 和 paddingRight
     * 设置 top bottom 相当于设置item的 marginTop 和 marginBottom
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0 || isFirstGroup(pos)) {
            outRect.top = mHeight;
        }
    }

    /**
     * 判断是否是分组的第一个
     */
    private boolean isFirstGroup(int pos) {
        if (pos == 0) {
            return true;
        }
        String preGroupName = listener.getGroupName(pos - 1);
        String nowGroupName = listener.getGroupName(pos);
        return !preGroupName.endsWith(nowGroupName);
    }

    public void setListener(GroupListener listener) {
        this.listener = listener;
    }
}
