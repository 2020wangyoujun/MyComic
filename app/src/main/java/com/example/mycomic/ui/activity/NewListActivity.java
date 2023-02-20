package com.example.mycomic.ui.activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.presenter.NewListPresenter;
import com.example.mycomic.ui.activity.base.BaseActivity;
import com.example.mycomic.ui.adapter.NewListAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.custom.ElasticImageScrollView;
import com.example.mycomic.ui.custom.NoScrollGridLayoutManager;
import com.example.mycomic.ui.view.INewView;
import com.example.mycomic.utils.IntentUtil;

import java.util.List;


public class NewListActivity extends BaseActivity<NewListPresenter> implements INewView<List<Comic>> {

    RecyclerView mRecyclerView;
    ElasticImageScrollView mScrollView;
    RelativeLayout mTitle;
    private NewListAdapter mAdapter;
    ImageView ivBackColor;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new NewListPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_new;
    }

    @Override
    protected void initView() {
        mAdapter= new NewListAdapter(this,R.layout.item_newlist,R.layout.item_loading);

        NoScrollGridLayoutManager gridLayoutManager = new NoScrollGridLayoutManager(this,1);
        gridLayoutManager.setScrollEnabled(false);
        mRecyclerView=findViewById(R.id.rv_bookshelf);
        mScrollView=findViewById(R.id.ev_scrollview);
        mTitle=findViewById(R.id.rl_title);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mScrollView.setListener(new ElasticImageScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadData();
            }

            @Override
            public void onAlphaActionBar(float a) {
                mTitle.setAlpha(a);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if(a!=0){
//                        initStatusBar(false);
//                    }else{
//                        initStatusBar(true);
//                    }
//                }
                if(a!=0){
                    initStatusBar(false);
                }else{
                    initStatusBar(true);
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if(position!=mAdapter.getItemCount()-1&&position>=0){
                    Comic comic = mAdapter.getItems(position);
                    IntentUtil.ToComicDetail(NewListActivity.this,comic.getId()+"",comic.getTitle());
                }
            }
        });

        ivBackColor=findViewById(R.id.iv_back_color);
        ivBackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(v);
            }
        });

        mPresenter.loadData();

    }


    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<Comic> data) {
        mAdapter.updateWithClear(data);
        mAdapter.notifyDataSetChanged();
        mScrollView.setInnerHeight();
    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void ShowToast(String t) {

    }

//    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        super.finish();
    }
}
