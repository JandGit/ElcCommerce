package com.android.tkengine.elccommerce.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {

    private View convertView;
    private SparseArray<View> mViewChildren;

    public ViewHolder(View itemView) {
        super(itemView);
        convertView = itemView;
        mViewChildren = new SparseArray<>();
    }

    public static ViewHolder createViewHolder(int layoutId, Context context, ViewGroup parent){
        return new ViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    public <T extends View> T getView(int viewId){
        View view = mViewChildren.get(viewId);
        if(null == view){
            view = convertView.findViewById(viewId);
            mViewChildren.put(viewId, view);
        }
        return (T)view;
    }

    public void setText(int id, String text){
        TextView tv = getView(id);
        tv.setText(text);
    }

    public void setImageResource(int id, int res){
        ImageView iv = getView(id);
        iv.setImageResource(res);
    }

    public void setImageBitmap(int id, Bitmap bitmap){
        ImageView iv = getView(id);
        iv.setImageBitmap(bitmap);
    }
}
