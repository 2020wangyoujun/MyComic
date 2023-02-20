package com.example.mycomic.ui.activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.presenter.RankPresenter;
import com.example.mycomic.ui.activity.base.BaseActivity;
import com.example.mycomic.ui.adapter.RankAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.custom.CustomTab;
import com.example.mycomic.ui.custom.ElasticScrollView;
import com.example.mycomic.ui.custom.NoScrollGridLayoutManager;
import com.example.mycomic.ui.view.IRankView;
import com.example.mycomic.utils.IntentUtil;

import java.util.List;


public class RankActivity extends BaseActivity<RankPresenter> implements IRankView<List<Comic>> {
    RecyclerView mRecycleView;
    RankAdapter mAdapter;
    ElasticScrollView mScrollView;
    CustomTab mTab;

    ImageView ivBackColor,ivSearch;
    RelativeLayout rl_update,rl_sellgood,rl_hot,rl_mouth;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new RankPresenter(this,this);
        mAdapter = new RankAdapter(this, R.layout.item_rank,R.layout.item_loading);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_rank;
    }

    @Override
    protected void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            initStatusBar(false);
//        }
        initStatusBar(false);
        mTab=findViewById(R.id.ll_actionbar);
        mPresenter.loadData();
        final NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(this,1);
        layoutManager.setScrollEnabled(false);
        mRecycleView=findViewById(R.id.rv_bookshelf);
        mScrollView=findViewById(R.id.ev_scrollview);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mAdapter);
        mScrollView.setListener(new ElasticScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadData();
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if(position!=mAdapter.getItemCount()-1){
                    Comic comic = mAdapter.getItems(position);
                    IntentUtil.ToComicDetail(RankActivity.this,comic.getId()+"",comic.getTitle());
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
        ivSearch=findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToSearch(v);
            }
        });

        View.OnClickListener rlListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getType(v);
            }
        };
        rl_update=findViewById(R.id.rl_update);
        rl_sellgood=findViewById(R.id.rl_sellgood);
        rl_hot=findViewById(R.id.rl_hot);
        rl_mouth=findViewById(R.id.rl_mouth);
        rl_update.setOnClickListener(rlListener);
        rl_sellgood.setOnClickListener(rlListener);
        rl_hot.setOnClickListener(rlListener);
        rl_mouth.setOnClickListener(rlListener);
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<Comic> data) {
        mAdapter.updateWithClear(data);
    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
        //mScrollView.setInnerHeight();
    }


    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

//    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        super.finish();
    }

//    @OnClick(R.id.iv_search)
    public void ToSearch(View view){
        IntentUtil.ToSearch(this);
    }

//    @OnClick({ R.id.rl_update, R.id.rl_sellgood, R.id.rl_hot,R.id.rl_mouth})
    public void getType(View view) {
        switch (view.getId()) {
            case R.id.rl_update:
                mPresenter.setType("upt");
                break;
            case R.id.rl_sellgood:
                mPresenter.setType("pay");
                break;
            case R.id.rl_hot:
                mPresenter.setType("pgv");
                break;
            case R.id.rl_mouth:
                mPresenter.setType("mt");
                break;
        }
    }

    @Override
    public void setType(int position) {
        mTab.setCurrentPosition(position);
    }
}
