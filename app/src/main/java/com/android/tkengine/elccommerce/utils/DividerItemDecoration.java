package com.android.tkengine.elccommerce.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.android.tkengine.elccommerce.presenter.CartFrgPresenter;

/**
 * Created by 陈嘉shuo on 2016/8/9.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int mOrientation = LinearLayoutManager.VERTICAL;   //RecyclerView纵向布局
    private int mItemSize = 1; //Item间分割线大小
    private Paint mPaint;  //分割线画笔

    public DividerItemDecoration(Context context, int orientation){
        this.mOrientation = orientation;
        //将单位(sp/dp/dip/px)转换
        mItemSize = (int) TypedValue.applyDimension(mItemSize, TypedValue.COMPLEX_UNIT_DIP,context.getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;  //用于绘制时抗锯齿
        mPaint.setColor(0xEEEEEEEE);
        mPaint.setStyle(Paint.Style.FILL); //设置填充

    }

    //此方法需要我们计算出绘制的分割线的【位置和范围】，并绘制在Canvas上。主要的逻辑就是通过parent获取到child，
    // 然后从child中获取到Item的四个边的位置，从而计算出位置和范围。
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation == LinearLayoutManager.VERTICAL){
            drawVertical(c,parent) ;
        }else {
            drawHorizontal(c,parent) ;
        }
    }


    //此方法主要是为了在每个Item的某一位置预留出分割线的空间 ，从而让Decoration绘制在预留的空间内。
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation == LinearLayoutManager.VERTICAL){
            outRect.set(mItemSize,0,0,0);
        }else {
            outRect.set(0,0,mItemSize,0);
        }
    }

    private void drawVertical(Canvas canvas,RecyclerView parent){
        final int left = parent.getPaddingLeft() ;
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight() ;
        final int childSize = parent.getChildCount() ;
        for(int i = 0 ; i < childSize ; i ++){
            final View child = parent.getChildAt( i ) ;
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin ;
            final int bottom = top + mItemSize ;
            canvas.drawRect(left,top,right,bottom,mPaint);
        }
    }

    private void drawHorizontal(Canvas canvas,RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for(int i= 0 ;i< childSize; i++){
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin ;
            final int right = left + mItemSize ;
            canvas.drawRect(left,top,right,bottom,mPaint);
        }
    }


    public void setSize(int size) {
        this.mItemSize = size;
    }


}
