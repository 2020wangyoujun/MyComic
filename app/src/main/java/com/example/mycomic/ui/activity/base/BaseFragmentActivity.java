package com.example.mycomic.ui.activity.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mycomic.R;
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;


import java.util.List;

public abstract class BaseFragmentActivity extends RxAppCompatActivity {

    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;
    protected List<Fragment> fragments;
    protected boolean isTrans;

    public boolean isTrans() {
        return isTrans;
    }

    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initStatusBar(true);
        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initStatusBar(boolean isTransparent) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if(isTransparent){
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }else{
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        isTrans = isTransparent;
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

    }

    public void selectTab(int num){
        fragmentTransaction = fragmentManager.beginTransaction();
        try {
            for(int i = 0;i < fragments.size(); i++){
                fragmentTransaction.hide(fragments.get(i));
            }
            fragmentTransaction.show(fragments.get(num));
        } catch (ArrayIndexOutOfBoundsException ae) {
            ae.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    protected abstract void initView();

    //获取Intent
    protected void handleIntent(Intent intent) {

    }

    protected int getContentViewId() {
        return R.layout.activity_base;
    }
}
