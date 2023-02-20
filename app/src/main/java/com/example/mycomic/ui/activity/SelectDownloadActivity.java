package com.example.mycomic.ui.activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomic.R;
import com.example.mycomic.presenter.SelectDownloadPresenter;
import com.example.mycomic.ui.activity.base.BaseActivity;
import com.example.mycomic.ui.adapter.SelectDownloadAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.view.ISelectDownloadView;

import java.util.HashMap;


public class SelectDownloadActivity extends BaseActivity<SelectDownloadPresenter> implements ISelectDownloadView {
    TextView mChapterNum;
    RecyclerView mRecycleView;
    ImageView mOrder;
    TextView mSelected;
    ImageView mSelectedIcon;
    TextView mSelectedNum;
    private SelectDownloadAdapter mAdapter;
    RelativeLayout rl_download,rl_select;
    ImageView ivBackColor,ivOrder;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new SelectDownloadPresenter(this,this,intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_download;
    }

    @Override
    protected void initView() {
        mChapterNum=findViewById(R.id.tv_chapters_num);
        mSelectedNum=findViewById(R.id.tv_selected);
        mSelected=findViewById(R.id.tv_select_all);
        mSelectedIcon=findViewById(R.id.iv_select);
        mOrder=findViewById(R.id.iv_order);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecycleView=findViewById(R.id.rv_index);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new SelectDownloadAdapter(this,R.layout.item_select_download);
        mAdapter.updateWithClear(mPresenter.getmChapters());
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if(!mAdapter.isOrder){
                    position = mPresenter.getmChapters().size()-position-1;
                }
                mPresenter.uptdateToSelected(position);
            }
        });
        mRecycleView.setAdapter(mAdapter);
        mChapterNum.setText("共"+mPresenter.getmChapters().size()+"话");

        rl_download=findViewById(R.id.rl_download);
        rl_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });
        ivBackColor=findViewById(R.id.iv_back_color);
        ivBackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(v);
            }
        });
        ivOrder=findViewById(R.id.iv_order);
        ivOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderList((ImageView) v);
            }
        });
        rl_select=findViewById(R.id.rl_select);
        rl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectAll(v);
            }
        });
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(Object data) {

    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }
    @Override
//    @OnClick(R.id.rl_download)
    public void startDownload() {
        mPresenter.startDownload();
    }

    @Override
    public void updateList(HashMap map) {
        mAdapter.setMap(map);
        mAdapter.notifyDataSetChanged();
        mSelectedNum.setText("已选择"+mPresenter.getSelectedNum()+"话");
    }

    @Override
    public void updateListItem(HashMap map, int position) {
        mAdapter.setMap(map);
        if(!mAdapter.isOrder()){
            position = map.size()-position-1;
        }
        mAdapter.notifyItemChanged(position,"isNotNull");
        mSelectedNum.setText("已选择"+mPresenter.getSelectedNum()+"话");
    }

    @Override
    public void addAll() {
        mSelected.setText("取消全选");
        mSelectedIcon.setImageResource(R.mipmap.btn_cancel_select);
    }

    @Override
    public void removeAll() {
        mSelected.setText("全选");
        mSelectedIcon.setImageResource(R.mipmap.btn_select);
    }

    @Override
    public void quitEdit() {

    }

//    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        this.finish();
    }

//    @OnClick({R.id.iv_order })
    public void OrderList(ImageView Order) {
        mAdapter.setOrder(!mAdapter.isOrder());
        if(!mAdapter.isOrder()){
            mOrder.setImageResource(R.mipmap.daoxu);
        }else{
            mOrder.setImageResource(R.mipmap.zhengxu);
        }
    }

//    @OnClick(R.id.rl_select)
    public void SelectAll(View view){
        mPresenter.SelectOrMoveAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getDataFormDb();
    }
}
