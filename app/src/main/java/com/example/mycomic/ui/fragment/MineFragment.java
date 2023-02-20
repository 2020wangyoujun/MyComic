package com.example.mycomic.ui.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.MineTitle;
import com.example.mycomic.presenter.MinePresenter;
import com.example.mycomic.ui.adapter.MineAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.fragment.base.BaseFragment;
import com.example.mycomic.ui.view.IMineView;

import java.util.List;


public class MineFragment  extends BaseFragment<MinePresenter> implements IMineView<List<MineTitle>> {
    RecyclerView mRecycle;
//    @Bind(R.id.iv_cover)
//    ImageView mCover;
    private MineAdapter mineAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new MinePresenter(getActivity(),this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mineAdapter = new MineAdapter(getActivity(), R.layout.item_mine);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        mRecycle=view.findViewById(R.id.rv_mine);
        mRecycle.setLayoutManager(layoutManager);
        mRecycle.setAdapter(mineAdapter);
        mineAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                mPresenter.onItemClick(position);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<MineTitle> data) {
        mineAdapter.updateWithClear(data);
    }

    @Override
    public void getDataFinish() {
        mineAdapter.notifyDataSetChanged();
    }
//    @OnClick(R.id.rl_information)
//    public void toGithub(){
//        IntentUtil.toUrl(getActivity(),"https://github.com/2020wangyoujun/MyComic");
//    }

    @Override
    public void SwitchSkin(boolean isNight) {
        mineAdapter.setNight(isNight);
//        mActivity.switchModel();
    }
}
