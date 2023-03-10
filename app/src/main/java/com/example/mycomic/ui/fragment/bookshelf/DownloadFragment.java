package com.example.mycomic.ui.fragment.bookshelf;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.presenter.DownloadPresenter;
import com.example.mycomic.ui.adapter.DownloadAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.custom.NoScrollGridLayoutManager;
import com.example.mycomic.ui.fragment.base.BaseBookShelfFragment;
import com.example.mycomic.ui.view.ICollectionView;
import com.example.mycomic.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadFragment extends BaseBookShelfFragment<DownloadPresenter> implements ICollectionView<List<Comic>>,BaseRecyclerAdapter.OnItemClickListener {
    RecyclerView mRecycleView;
    RelativeLayout mEmptyView;
    private DownloadAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new DownloadPresenter(mActivity,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_download;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mEmptyView=view.findViewById(R.id.rl_empty_view);

        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(mActivity,1);
        layoutManager.setScrollEnabled(false);
        mRecycleView=view.findViewById(R.id.rv_bookshelf);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new DownloadAdapter(mActivity,R.layout.item_download);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }
    //切换到该fragment做的操作
   public void onHiddenChanged(boolean hidden) {
       super.onHiddenChanged(hidden);
       if (!hidden) {// 不在最前端界面显示
           mPresenter.loadData();
       }
   }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadData();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void showErrorView(String throwable) {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void fillData(List<Comic> data) {
        mAdapter.updateWithClear(data);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {
        mAdapter.updateWithClear(new ArrayList<Comic>());
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        if(!mAdapter.isEditing()){
            Comic comic = mAdapter.getItems(position);
            IntentUtil.ToDownloadListActivity(mActivity,new HashMap<Integer, Integer>(),comic);
        }else{
            mPresenter.uptdateToSelected(position);
        }
    }

    @Override
    public void addAll() {
        mainActivity.getmEditBottom().addAll();
    }

    @Override
    public void removeAll() {
        mainActivity.getmEditBottom().removeAll();
    }

    public void OnEditList(boolean isEdit){
        if(mAdapter!=null&&mAdapter.isEditing()!=isEdit){
            mPresenter.clearSelect();
            mAdapter.setEditing(isEdit);
        }
    }

    @Override
    public void OnDelete() {
        mPresenter.ShowDeteleDialog();
    }

    @Override
    public void OnSelect() {
        mPresenter.SelectOrMoveAll();
    }

    @Override
    public void updateList(HashMap map) {
        mAdapter.setmMap(map);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateListItem(HashMap map, int position) {
        mAdapter.setmMap(map);
        mAdapter.notifyItemChanged(position,"isNotNull");
    }

    @Override
    public void quitEdit() {
        mainActivity.quitEdit();
    }
}
