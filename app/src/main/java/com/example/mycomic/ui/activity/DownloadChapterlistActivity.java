package com.example.mycomic.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomic.R;
import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.db.DBChapters;
import com.example.mycomic.presenter.DownloadChapterlistPresenter;
import com.example.mycomic.ui.activity.base.BaseActivity;
import com.example.mycomic.ui.adapter.DownloadChapterlistAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.custom.FloatEditLayout;
import com.example.mycomic.ui.view.IDownloadlistView;
import com.example.mycomic.utils.IntentUtil;

import java.util.HashMap;
import java.util.List;


public class DownloadChapterlistActivity extends BaseActivity<DownloadChapterlistPresenter> implements IDownloadlistView<List<DBChapters>> {
    TextView mTitle;
    RecyclerView mRecyclerview;
    RelativeLayout mRLloading;
    TextView mLoadingText;
    ImageView mReload;
    ImageView mLoading;
    TextView mDownloadText;
    ImageView mDownloadImage;
    ImageView mEdit;
    FloatEditLayout mEditLayout;
    private DownloadChapterlistAdapter mAdapter;
    private int recycleState = 0;
    ImageView ivBackColor;
    RelativeLayout rlAll,rlOrder;

    @Override
    protected void initPresenter(Intent intent) {
        mTitle=findViewById(R.id.tv_title);
        mPresenter = new DownloadChapterlistPresenter(this,this,intent);
        mTitle.setText(((Comic)intent.getSerializableExtra(Constants.COMIC)).getTitle());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_downloadlist;
    }

    @Override
    protected void initView() {
        mDownloadText=findViewById(R.id.tv_download);
        mDownloadImage=findViewById(R.id.iv_download);
        mEdit=findViewById(R.id.iv_edit);
        mRLloading=findViewById(R.id.rl_loading);
        mReload=findViewById(R.id.iv_error);
        mLoadingText=findViewById(R.id.tv_loading);

        mLoading=findViewById(R.id.iv_loading);
        //???????????????
        mLoading.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoading.getDrawable();
        animationDrawable.start();
        //
        mAdapter = new DownloadChapterlistAdapter(this,R.layout.item_downloadlist);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        mRecyclerview=findViewById(R.id.rv_downloadlist);
        mRecyclerview.setLayoutManager(layoutmanager);
        mRecyclerview.setAdapter(mAdapter);
        //mPresenter.initDbData();
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //????????????????????????
                recycleState = newState;
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                DBChapters info = mAdapter.getItems(position);
                mPresenter.onItemClick(info,position);
            }
        });
        mEditLayout=findViewById(R.id.rl_edit_bottom);
        mEditLayout.setListener(new FloatEditLayout.onClickListener() {
            @Override
            public void OnClickSelect() {
                mPresenter.SelectOrMoveAll();
            }

            @Override
            public void OnDelete() {
                mPresenter.ShowDeteleDialog();
            }
        });

        ivBackColor=findViewById(R.id.iv_back_color);
        ivBackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(v);
            }
        });
        rlAll=findViewById(R.id.rl_all);
        rlAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseOrStartAll();
            }
        });
        mReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload(v);
            }
        });
        rlOrder=findViewById(R.id.rl_order);
        rlOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToSelectDownload();
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
    protected void onResume() {
        super.onResume();
        mPresenter.initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initPresenter(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.pauseAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.updateComic();
    }

    @Override
    public void onLoadMoreData(List<DBChapters> downInfos) {

    }

    @Override
    public void updateView(int postion) {
        //???????????????????????????????????????????????????????????????
        if(recycleState == 0){
            //??????????????????????????????????????????
            mAdapter.notifyItemChanged(postion,"isNotEmpty");
        }else{
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDownloadFinished() {
        mDownloadText.setText("????????????");
        mDownloadImage.setVisibility(View.GONE);
        mPresenter.isAllDownload = DownloadChapterlistPresenter.FINISH;
    }


//    @OnClick(R.id.rl_all)
    @Override
    public void onPauseOrStartAll() {
        switch (mPresenter.isAllDownload){
            case DownloadChapterlistPresenter.DOWNLOADING:
                mPresenter.stopAll();
                mDownloadText.setText("????????????");
                mDownloadImage.setImageResource(R.mipmap.pasue);
                mPresenter.isAllDownload = DownloadChapterlistPresenter.STOP_DOWNLOAD;
                break;
            case DownloadChapterlistPresenter.STOP_DOWNLOAD:
                mPresenter.ReStartAll();
                mDownloadText.setText("????????????");
                mDownloadImage.setImageResource(R.mipmap.pasue_select);
                mPresenter.isAllDownload = DownloadChapterlistPresenter.DOWNLOADING;
                break;
            default:
                break;
        }
    }

    @Override
    public void updateList(HashMap map) {
        mAdapter.setmMap(map);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateListItem(HashMap map, int position) {
        mAdapter.setmMap(map);
        mAdapter.notifyItemChanged(position,"isNotEmpty");
    }

    @Override
    public void addAll() {
        mEditLayout.addAll();
    }

    @Override
    public void removeAll() {
        mEditLayout.removeAll();
    }

    @Override
    public void quitEdit() {
        mEdit.setImageResource(R.mipmap.edit);
        mEditLayout.setVisibility(View.GONE);
        mPresenter.setEditing(false);
        mAdapter.setEditing(mPresenter.isEditing());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

//    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        this.finish();
    }

    @Override
    public void showErrorView(String throwable) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        mLoadingText.setText(throwable);
    }

    @Override
    public void fillData(List<DBChapters> mLists) {
        if(mLists!=null&&mLists.size()!=0){
            mAdapter.updateWithClear(mLists);
            mAdapter.notifyDataSetChanged();
            //??????????????????
            mPresenter.startAll();
        }
        mRLloading.setVisibility(View.GONE);

    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
    }

//    @OnClick(R.id.iv_error)
    public void reload(View view){
        mPresenter.initData();
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
        mLoadingText.setText("??????????????????????????????");
    }
//    @OnClick(R.id.rl_order)
    public void ToSelectDownload(){
        IntentUtil.ToSelectDownload(this,mPresenter.getmComic());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.getResultComic(resultCode, data);
    }

//    @OnClick(R.id.iv_edit)
    public void toEdit(){
        if(!mPresenter.isEditing()){
            mEdit.setImageResource(R.mipmap.closed);
            mEditLayout.setVisibility(View.VISIBLE);
        }else{
            mEdit.setImageResource(R.mipmap.edit);
            mEditLayout.setVisibility(View.GONE);
        }
        mPresenter.setEditing(!mPresenter.isEditing());
        mAdapter.setEditing(mPresenter.isEditing());
        mAdapter.notifyDataSetChanged();
    }
}
