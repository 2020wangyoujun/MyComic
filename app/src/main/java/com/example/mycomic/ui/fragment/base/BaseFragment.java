package com.example.mycomic.ui.fragment.base;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mycomic.presenter.BasePresenter;
import com.example.mycomic.ui.activity.base.BaseFragmentActivity;
import com.example.mycomic.utils.ZToast;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    protected P mPresenter;
    //初始化Presenter
    protected abstract void initPresenter();

    protected BaseFragmentActivity mActivity;

    protected abstract void initView(View view, Bundle savedInstanceState);

    //获取布局文件ID
    protected abstract int getLayoutId();

    //获取宿主Activity
    protected BaseFragmentActivity getHoldingActivity() {
        return mActivity;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity=(BaseFragmentActivity)requireActivity();
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        this.mActivity = (BaseFragmentActivity) activity;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initPresenter();
        checkPresenterIsNull();
        initView(view, savedInstanceState);
        return view;
    }

    private void checkPresenterIsNull() {
        if (mPresenter == null) {
            throw new IllegalStateException("please init mPresenter in initPresenter() method ");
        }
    }

    public void showToast(String text){
        ZToast.makeText(mActivity, text,1000).show();
        //Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
    }
}
