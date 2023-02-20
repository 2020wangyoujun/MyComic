package com.example.mycomic.ui.custom;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class NoScrollStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    private boolean isScrollEnabled = true;

    public NoScrollStaggeredGridLayoutManager(int num,int Orientation) {
        super(num,Orientation);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
