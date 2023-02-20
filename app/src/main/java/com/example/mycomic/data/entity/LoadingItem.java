package com.example.mycomic.data.entity;


public class LoadingItem extends Comic{
    boolean isLoading;
    public LoadingItem(boolean isLoading){
        this.isLoading = isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }
}
