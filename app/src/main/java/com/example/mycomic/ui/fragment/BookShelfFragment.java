package com.example.mycomic.ui.fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mycomic.ui.activity.MainActivity;
import com.example.mycomic.R;
import com.example.mycomic.presenter.BookShelfPresenter;
import com.example.mycomic.ui.adapter.BookShelfFragmentAdapter;
import com.example.mycomic.ui.fragment.base.BaseBookShelfFragment;
import com.example.mycomic.ui.fragment.base.BaseFragment;
import com.example.mycomic.ui.fragment.bookshelf.CollectionFragment;
import com.example.mycomic.ui.fragment.bookshelf.DownloadFragment;
import com.example.mycomic.ui.fragment.bookshelf.HistoryFragment;
import com.example.mycomic.ui.view.IBookShelfView;

import java.util.ArrayList;
import java.util.List;

public class BookShelfFragment extends BaseFragment<BookShelfPresenter> implements IBookShelfView {
    ViewPager2 mViewpager;
    BookShelfFragmentAdapter mAdapter;
    protected FragmentManager fragmentManager;
    protected List<BaseBookShelfFragment> fragments;
    TextView mDownload;
    TextView mHistory;
    TextView mCollect;
    ImageView mBottomCollect;
    ImageView mBottomHistory;
    ImageView mBottomDownload;
    ImageView mEdit;
    private boolean isEditing;
    private MainActivity mainActivity;
    private CollectionFragment collectionFragment;
    private HistoryFragment historyFragment;
    private DownloadFragment downloadFragment;

    private RelativeLayout rlCollect,rlHistory,rlDownload;


    @Override
    protected void initPresenter() {
        mPresenter = new BookShelfPresenter(mActivity,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mCollect=view.findViewById(R.id.tv_collect);
        mBottomCollect=view.findViewById(R.id.iv_bottom_collect);
        mHistory=view.findViewById(R.id.tv_history);
        mBottomHistory=view.findViewById(R.id.iv_bottom_history);
        mDownload=view.findViewById(R.id.tv_download);
        mBottomDownload=view.findViewById(R.id.iv_bottom_download);

        fragments = new ArrayList<>();
        mainActivity = (MainActivity) this.getActivity();

        collectionFragment = new CollectionFragment();
        historyFragment = new HistoryFragment();
        downloadFragment = new DownloadFragment();


        fragments.add(collectionFragment);
        fragments.add(historyFragment);
        fragments.add(downloadFragment);

        fragmentManager = getActivity().getSupportFragmentManager();
        mAdapter = new BookShelfFragmentAdapter(fragmentManager,getLifecycle(),fragments);
        mViewpager=view.findViewById(R.id.vp_bookshelf);
        mEdit=view.findViewById(R.id.iv_edit);
        mViewpager.setAdapter(mAdapter);
        mViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(isEditing){
                    mEdit.setImageResource(R.mipmap.edit);
                    mainActivity.setEditBottomVisible(View.GONE);
                    for(int i=0;i<fragments.size();i++){
                        fragments.get(i).OnEditList(false);
                    }
                    isEditing = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        ToCollect();
                        break;
                    case 1:
                        ToHistory();
                        break;
                    case 2:
                        ToDownload();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
            }
        });
//        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if(isEditing){
//                    mEdit.setImageResource(R.mipmap.edit);
//                    mainActivity.setEditBottomVisible(View.GONE);
//                    for(int i=0;i<fragments.size();i++){
//                        fragments.get(i).OnEditList(false);
//                    }
//                    isEditing = false;
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position){
//                    case 0:
//                        ToCollect();
//                        break;
//                    case 1:
//                        ToHistory();
//                        break;
//                    case 2:
//                        ToDownload();
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        rlCollect=view.findViewById(R.id.rl_collect);
        rlHistory=view.findViewById(R.id.rl_history);
        rlDownload=view.findViewById(R.id.rl_download);
        rlCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToCollect();
            }
        });
        rlHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToHistory();
            }
        });
        rlDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDownload();
            }
        });
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEdit();
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }
//    @OnClick(R.id.rl_collect)
    public void ToCollect(){
        ResetTitle();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mCollect.setTextAppearance(R.style.colorTextBlack);
//        }else{
//            mCollect.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorTextColorDark));
//        }
        mCollect.setTextAppearance(R.style.colorTextBlack);
        mViewpager.setCurrentItem(0);
        mBottomCollect.setVisibility(View.VISIBLE);

    }
//    @OnClick(R.id.rl_history)
    public void ToHistory(){
        ResetTitle();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mHistory.setTextAppearance(R.style.colorTextBlack);
//        }else{
//            mHistory.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorTextColorDark));
//        }
        mHistory.setTextAppearance(R.style.colorTextBlack);
        mViewpager.setCurrentItem(1);
        mBottomHistory.setVisibility(View.VISIBLE);
    }
//    @OnClick(R.id.rl_download)
    public void ToDownload(){
        ResetTitle();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mDownload.setTextAppearance(R.style.colorTextBlack);
//        }else{
//            mDownload.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorTextColorDark));
//        }
        mDownload.setTextAppearance(R.style.colorTextBlack);
        mViewpager.setCurrentItem(2);
        mBottomDownload.setVisibility(View.VISIBLE);
    }

    public void ResetTitle(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDownload.setTextAppearance(R.style.colorTextColorLight);
            mCollect.setTextAppearance(R.style.colorTextColorLight);
            mHistory.setTextAppearance(R.style.colorTextColorLight);
        }else{
            mDownload.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorTextColorLight));
            mCollect.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorTextColorLight));
            mHistory.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorTextColorLight));
        }
        mBottomCollect.setVisibility(View.GONE);
        mBottomDownload.setVisibility(View.GONE);
        mBottomHistory.setVisibility(View.GONE);
    }
//    @OnClick(R.id.iv_edit)
    public void toEdit(){
        if(!isEditing){
            mEdit.setImageResource(R.mipmap.closed);
            mainActivity.setEditBottomVisible(View.VISIBLE);
            showEditModel(fragments.get(mViewpager.getCurrentItem()),true);
        }else{
            mEdit.setImageResource(R.mipmap.edit);
            mainActivity.setEditBottomVisible(View.GONE);
            showEditModel(fragments.get(mViewpager.getCurrentItem()),false);
        }
        isEditing = !isEditing;
    }

    public void quitEdit(){
        mEdit.setImageResource(R.mipmap.edit);
        showEditModel(fragments.get(mViewpager.getCurrentItem()),false);
        isEditing = false;
    }

    private void showEditModel(BaseBookShelfFragment fragment, boolean isEdit) {
        fragment.OnEditList(isEdit);
    }

    public void OnClickDelete(){
        fragments.get(mViewpager.getCurrentItem()).OnDelete();
    }


    public void OnClickSelect(){
        fragments.get(mViewpager.getCurrentItem()).OnSelect();
    }
}
