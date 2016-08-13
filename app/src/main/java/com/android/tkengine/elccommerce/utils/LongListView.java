package com.android.tkengine.elccommerce.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class LongListView extends ListView {
    public LongListView(Context context) {
        super(context);
    }

    public LongListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LongListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LongListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int newHeightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, newHeightSpec);
    }
}
