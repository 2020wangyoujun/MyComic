package com.example.mycomic.ui.activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.Type;
import com.example.mycomic.presenter.CategoryPresenter;
import com.example.mycomic.ui.activity.base.BaseActivity;
import com.example.mycomic.ui.adapter.CategoryAdapter;
import com.example.mycomic.ui.adapter.CategoryListAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.custom.ElasticHeadScrollView;
import com.example.mycomic.ui.custom.NoScrollGridLayoutManager;
import com.example.mycomic.ui.view.ICategoryView;
import com.example.mycomic.utils.IntentUtil;

import java.util.List;
import java.util.Map;

public class CategoryActivity extends BaseActivity<CategoryPresenter> implements ICategoryView<List<Comic>> {
    RecyclerView mSelectRecyclerView;
    RecyclerView mSelectListRecyclerView;
    ElasticHeadScrollView mScrollView;
    TextView mCategoryText;
    RelativeLayout mCategoryRelativeLayout;
    CategoryAdapter mSelectAdapter;
    CategoryListAdapter mCategoryAdapter;

    ImageView ivBackColor,ivSearch;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new CategoryPresenter(this,this,intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_category;
    }

    @Override
    protected void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            initStatusBar(false);
//        }
        initStatusBar(false);
        mCategoryRelativeLayout=findViewById(R.id.rl_actionbar_category);
        mCategoryText=findViewById(R.id.tv_actionbar_category);

        mSelectAdapter = new CategoryAdapter(this,R.layout.item_categroy_select);
        mCategoryAdapter = new CategoryListAdapter(this,R.layout.item_homepage_three,R.layout.item_loading);

        NoScrollGridLayoutManager gridLayoutManager = new NoScrollGridLayoutManager(this,7);
        gridLayoutManager.setScrollEnabled(false);
        mSelectRecyclerView=findViewById(R.id.rv_select);
        mSelectListRecyclerView=findViewById(R.id.rv_bookshelf);
        mSelectRecyclerView.setLayoutManager(gridLayoutManager);
        mSelectRecyclerView.setAdapter(mSelectAdapter);


        NoScrollGridLayoutManager ListgridLayoutManager = new NoScrollGridLayoutManager(this,3);
        ListgridLayoutManager.setScrollEnabled(false);
        mSelectListRecyclerView.setLayoutManager(ListgridLayoutManager);
        mSelectListRecyclerView.setAdapter(mCategoryAdapter);

        mScrollView=findViewById(R.id.ev_scrollview);

        mSelectAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Type type = mSelectAdapter.getItems(position);
                mPresenter.onItemClick(type);
            }
        });

        mScrollView.setListener(new ElasticHeadScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadCategoryList();
            }

            @Override
            public void onAlphaActionBar(float a) {
                mCategoryRelativeLayout.setAlpha(a);
            }
        });

        mCategoryAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if(position!=mCategoryAdapter.getItemCount()-1&&position>=0){//蒲公英carsh修改，防止数组越界
                    Comic comic = mCategoryAdapter.getItems(position);
                    IntentUtil.ToComicDetail(CategoryActivity.this,comic.getId()+"",comic.getTitle());
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

        mPresenter.loadData();
    }

    @Override
    public void fillSelectData(List<Type> mList, Map<String, Integer> mMap) {
        mSelectAdapter.setSelectMap(mMap);
        mSelectAdapter.updateWithClear(mList);
        mSelectAdapter.notifyDataSetChanged();
        mCategoryText.setText(mPresenter.getTitle());
        //mScrollView.setInnerHeight();
    }

    @Override
    public void setMap(Map<String, Integer> mMap) {
        mSelectAdapter.setSelectMap(mMap);
        mCategoryText.setText(mPresenter.getTitle());

    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<Comic> data) {
        mCategoryAdapter.updateWithClear(data);
        mCategoryAdapter.notifyDataSetChanged();
        //mScrollView.setInnerHeight();
    }

    @Override
    public void getDataFinish() {

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
}
