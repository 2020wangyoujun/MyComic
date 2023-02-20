package com.example.mycomic.ui.fragment.bookshelf;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.HomeTitle;
import com.example.mycomic.data.entity.LoadingItem;
import com.example.mycomic.presenter.HistoryPresenter;
import com.example.mycomic.ui.adapter.HistoryAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.custom.ElasticScrollView;
import com.example.mycomic.ui.custom.NoScrollGridLayoutManager;
import com.example.mycomic.ui.fragment.base.BaseBookShelfFragment;
import com.example.mycomic.ui.view.ICollectionView;
import com.example.mycomic.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryFragment extends BaseBookShelfFragment<HistoryPresenter> implements ICollectionView<List<Comic>>, BaseRecyclerAdapter.OnItemClickListener {
    RecyclerView mRecycleView;
    private HistoryAdapter mAdapter;
    ElasticScrollView mScrollView;
    RelativeLayout mEmptyView;


    @Override
    protected void initPresenter() {
        mPresenter = new HistoryPresenter(mActivity,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mEmptyView=view.findViewById(R.id.rl_empty_view);

        final NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(mActivity,1);
        layoutManager.setScrollEnabled(false);
        mRecycleView=view.findViewById(R.id.rv_bookshelf);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new HistoryAdapter(mActivity,R.layout.item_history,R.layout.item_history_title,R.layout.item_loading);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mScrollView=view.findViewById(R.id.ev_scrollview);
        mScrollView.setListener(new ElasticScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadMoreData();
            }
        });
       /* mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                    if(lastVisiblePosition >= layoutManager.getItemCount() - 1){
                        mPresenter.loadData();
                    }
                }
            }
        });*/

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
        ShowToast("重新加载");
    }

    @Override
    public void fillData(List<Comic> data) {
        mEmptyView.setVisibility(View.GONE);
        mAdapter.updateWithClear(data);
        //mScrollView.setInnerHeight();
    }

    @Override
    public void showEmptyView() {
        mAdapter.updateWithClear(new ArrayList<Comic>());
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        if(mAdapter.getItems(position) instanceof HomeTitle || mAdapter.getItems(position) instanceof LoadingItem){

        }else if(!mAdapter.isEditing()){
            Comic comic = mAdapter.getItems(position);
            IntentUtil.ToComicChapter(mActivity,comic.getCurrentChapter(),comic);
        }else{
            mPresenter.uptdateToSelected(position);
        }
    }

    public void OnEditList(boolean isEdit){
        if(mAdapter.isEditing()!=isEdit){
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
    public void addAll() {
        mainActivity.getmEditBottom().addAll();
    }

    @Override
    public void removeAll() {
        mainActivity.getmEditBottom().removeAll();
    }

    @Override
    public void quitEdit() {
            mainActivity.quitEdit();
    }
}
