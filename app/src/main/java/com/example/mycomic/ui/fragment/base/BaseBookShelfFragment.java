package com.example.mycomic.ui.fragment.base;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.mycomic.ui.activity.MainActivity;
import com.example.mycomic.presenter.BasePresenter;


public abstract class BaseBookShelfFragment<P extends BasePresenter> extends BaseFragment<P>{
    protected MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity=(MainActivity)requireActivity();
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mainActivity = (MainActivity) getActivity();
//    }

    public abstract void OnEditList(boolean isEditing);
    public abstract void OnDelete();
    public abstract void OnSelect();
}
