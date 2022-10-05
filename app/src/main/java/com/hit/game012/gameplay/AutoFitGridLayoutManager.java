package com.hit.game012.gameplay;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AutoFitGridLayoutManager extends GridLayoutManager {
    private boolean columnWidthChanged = true;
    private int columnWidth;

    public AutoFitGridLayoutManager(Context context, int spanCount) {
        super(context, 1);
        setColumnWidth(spanCount);
    }
    private void setColumnWidth (int columnWidth){
        if (columnWidth >0 && columnWidth!=this.columnWidth){
            this.columnWidth = columnWidth;
            columnWidthChanged = true;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (columnWidthChanged && columnWidth>0){
            int totalSpace;
            if (getOrientation() == VERTICAL){
                totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
            } else {
                totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            }
            int spanCount = Math.max(1, totalSpace/columnWidth);
            setSpanCount(spanCount);
            columnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}
