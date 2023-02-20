package com.example.mycomic.ui.adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mycomic.ui.fragment.base.BaseBookShelfFragment;

import java.util.List;

public class BookShelfFragmentAdapter extends FragmentStateAdapter {

    private List<BaseBookShelfFragment> fraglist;
    public BookShelfFragmentAdapter(FragmentManager fm, Lifecycle lifecycle, List<BaseBookShelfFragment> fraglist) {
        super(fm,lifecycle);
        this.fraglist=fraglist;
    }

//    @Override
//    public Fragment getItem(int arg0) {
//        return fraglist.get(arg0);
//    }
//
//    @Override
//    public int getCount() {
//        return fraglist.size();
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fraglist.get(position);
    }

    @Override
    public int getItemCount() {
        return fraglist.size();
    }
}
