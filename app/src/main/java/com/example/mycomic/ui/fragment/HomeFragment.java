package com.example.mycomic.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomic.ui.activity.MainActivity;
import com.example.mycomic.R;
import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.presenter.HomePresenter;
import com.example.mycomic.ui.adapter.BannerAdapter;
import com.example.mycomic.ui.adapter.MainAdapter;
import com.example.mycomic.ui.custom.NoScrollGridLayoutManager;
import com.example.mycomic.ui.custom.ZElasticRefreshScrollView;
import com.example.mycomic.ui.fragment.base.BaseFragment;
import com.example.mycomic.ui.view.IHomeView;
import com.example.mycomic.utils.DisplayUtil;
import com.example.mycomic.utils.IntentUtil;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView<Comic>,MainAdapter.OnItemClickListener {
    RecyclerView mRecycleView;
    ZElasticRefreshScrollView mScrollView;
    RelativeLayout mErrorView;
    ImageView mReload;
    Banner mBanner;
    private MainAdapter mAdapter;
    RelativeLayout mActionBar;
    View mActionBarBg;
    TextView mTvRecent;
    RelativeLayout mRlRecent;
    TextView mHomeTitle1;
    TextView mHomeTitle2;
    ImageView mSearch;
    LinearLayout ll_category1,ll_category2,ll_category3;
    ImageView mIvRecent;

    MainActivity activity;
    BannerAdapter bannerAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter(getActivity(),this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        mBanner=view.findViewById(R.id.B_banner);
        //设置图片加载器
//        mBanner.setImageLoader(new GlideImageLoader());
//        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        bannerAdapter=new BannerAdapter(new ArrayList<>());
        mBanner.setAdapter(bannerAdapter).addBannerLifecycleObserver(requireActivity()).setIndicator(new CircleIndicator(requireContext()));


        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(getActivity(),6);
        layoutManager.setScrollEnabled(false);
        mRecycleView=view.findViewById(R.id.recycle_view);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(mActivity,R.layout.item_hometitle,R.layout.item_homepage_three,R.layout.item_homepage,R.layout.item_homepage_full);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        //mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mPresenter.LoadData();

        mScrollView=view.findViewById(R.id.sv_comic);
        mActionBar=view.findViewById(R.id.rl_actionbar);
        mHomeTitle1=view.findViewById(R.id.tv_hometitle1);
        mHomeTitle2=view.findViewById(R.id.tv_hometitle2);
        mSearch=view.findViewById(R.id.iv_search);
        mActionBarBg=view.findViewById(R.id.v_actionbar_bg);
        mScrollView.setRefreshListener(new ZElasticRefreshScrollView.RefreshListener() {
            @Override
            public void onActionDown() {
                mBanner.stop();
            }

            @Override
            public void onActionUp() {
                mBanner.start();
            }

            @Override
            public void onRefresh() {
                mPresenter.LoadData();
                //mPresenter.refreshData();
            }

            @Override
            public void onRefreshFinish() {
                mBanner.start();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadMore() {
                /*mPresenter.loadMoreData(i);
                i++;*/
            }

            @Override
            public void onScroll(int y) {
                if(y == ZElasticRefreshScrollView.SCROLL_TO_DOWN){
                    if(mActionBar.getVisibility() == View.VISIBLE){
                        mActionBar.setVisibility(View.GONE);
                        AnimationSet animationSet = new AnimationSet(true);
                        TranslateAnimation trans = new TranslateAnimation(0, 0 , 0, -DisplayUtil.dip2px(mActivity.getApplicationContext(),60) );
                        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
                        animationSet.addAnimation(trans);
                        animationSet.addAnimation(alphaAnimation);
                        animationSet.setDuration(200);
                        mActionBar.startAnimation(animationSet);
                    }
                }else{
                    if(mActionBar.getVisibility() == View.GONE){
                        mActionBar.setVisibility(View.VISIBLE);
                        AnimationSet animationSet = new AnimationSet(true);
                        TranslateAnimation trans = new TranslateAnimation(0, 0 , -DisplayUtil.dip2px(mActivity.getApplicationContext(),60), 0 );
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                        animationSet.addAnimation(trans);
                        animationSet.addAnimation(alphaAnimation);
                        animationSet.setDuration(200);
                        mActionBar.startAnimation(animationSet);
                    }
                }

            }

            @Override
            public void onAlphaActionBar(float a) {
                if(a<1){
                    mHomeTitle1.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorWhite));
                    mHomeTitle2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorWhite));
                    mSearch.setImageResource(R.mipmap.search);
                }else{
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        mHomeTitle1.setTextAppearance(R.style.colorTextBlack);
//                    }else{
//                        mHomeTitle1.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorTextBlack));
//                    }
                    mHomeTitle1.setTextAppearance(R.style.colorTextBlack);
                    mHomeTitle2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                    mSearch.setImageResource(R.mipmap.search_color);
                }
                mActionBarBg.setAlpha(a);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if(a==1){
//                        if(activity.isTrans()){
//                            activity.initStatusBar(false);
//                        }
//                    }else{
//                        if(!activity.isTrans()){
//                            activity.initStatusBar(true);
//                        }
//                    }
//                }
                if(a==1){
                    if(activity.isTrans()){
                        activity.initStatusBar(false);
                    }
                }else{
                    if(!activity.isTrans()){
                        activity.initStatusBar(true);
                    }
                }

            }
        });
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(Object data, int position) {
                Comic comic = mPresenter.getmBanners().get(position);
                IntentUtil.ToComicDetail(mActivity,comic.getId()+"",comic.getTitle());
            }

//            @Override
//            public void OnBannerClick(int position) {
//                Comic comic = mPresenter.getmBanners().get(position);
//                IntentUtil.ToComicDetail(mActivity,comic.getId()+"",comic.getTitle());
//            }
        });
        mErrorView=view.findViewById(R.id.rl_error_view);
        mRlRecent=view.findViewById(R.id.rl_recent);
        mTvRecent=view.findViewById(R.id.tv_recent);

        mReload=view.findViewById(R.id.iv_error);
        mReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReloadData(v);
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToSearch(v);
            }
        });

        ll_category1=view.findViewById(R.id.ll_category1);
        ll_category2=view.findViewById(R.id.ll_category2);
        ll_category3=view.findViewById(R.id.ll_category3);
        View.OnClickListener llCategoryListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCategory(v);
            }
        };
        ll_category1.setOnClickListener(llCategoryListener);
        ll_category2.setOnClickListener(llCategoryListener);
        ll_category3.setOnClickListener(llCategoryListener);

        mIvRecent=view.findViewById(R.id.iv_recent);
        mIvRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickClose();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @Override
    public void appendMoreDataToView(List<Comic> data) {
        if(data!=null&&data.size()!=0){
            mAdapter.update(data);
        }else {
            ShowToast("未取到数据");
        }
    }

    @Override
    public void hasNoMoreData() {
        ShowToast("没有数据了");
    }

    @Override
    public void showErrorView(String throwable) {
        mScrollView.setRefreshing(false);
        mErrorView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
    }

    @Override
    public void fillData(List<Comic> data) {
        if(data!=null&&data.size()!=0){
            mAdapter.updateWithClear(data);
        }else {
            ShowToast("未取到数据");
        }
    }

    @Override
    public void getDataFinish() {
        mScrollView.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
        mErrorView.setVisibility(View.GONE);
        mRecycleView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefreshFinish() {
        mScrollView.setRefreshing(false);
        if(mErrorView.isShown()){
            mErrorView.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void fillBanner(List<Comic> data) {
        bannerAdapter.setDatas(data);
        mBanner.start();
    }

    @Override
    public void fillRecent(String str) {
        if(str!=null){
            mRlRecent.setVisibility(View.VISIBLE);
            mTvRecent.setText(str);
        }else{
            mRlRecent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        Comic comic = mAdapter.getItems(position);
        IntentUtil.ToComicDetail(mActivity,comic.getId()+"",comic.getTitle());
    }

    @Override
    public void onTitleClick(RecyclerView parent, View view, int type) {
        switch (type){
            case Constants.TYPE_RANK_LIST:
                IntentUtil.toRankActivity(getActivity());
                break;
            case Constants.TYPE_RECOMMEND:
                ShowToast("更多热门推荐开发中");
                break;
            case Constants.TYPE_GIRL_RANK:
                IntentUtil.toCategoryActivity(getActivity(),Constants.CATEGORY_TITLE_AUDIENCE,2);
                break;
            case Constants.TYPE_BOY_RANK:
                IntentUtil.toCategoryActivity(getActivity(),Constants.CATEGORY_TITLE_AUDIENCE,1);
                break;
            case Constants.TYPE_HOT_SERIAL:
                IntentUtil.toCategoryActivity(getActivity(),Constants.CATEGORY_TITLE_FINISH,1);
                break;
            case Constants.TYPE_HOT_JAPAN:
                IntentUtil.toCategoryActivity(getActivity(),Constants.CATEGORY_TITLE_NATION,4);
                break;
            default:
                ShowToast("开发中");
                break;
        }
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mScrollView.scrollTo(0,0);
        }
    }

//    @OnClick(R.id.iv_error)
    public void ReloadData(View view){
        mErrorView.setVisibility(View.GONE);
        //mPresenter.refreshData();
        mPresenter.LoadData();
    }

//    @OnClick(R.id.iv_search)
    public void ToSearch(View view){
        IntentUtil.ToSearch(getActivity());
    }

//    @OnClick({ R.id.ll_category1, R.id.ll_category2, R.id.ll_category3})
    public void toCategory(View view) {
        switch (view.getId()) {
            case R.id.ll_category1:
                IntentUtil.toRankActivity(getActivity());
                break;
            case R.id.ll_category2:
                IntentUtil.toCategoryActivity(getActivity());
                break;
            case R.id.ll_category3:
                IntentUtil.toNewActivity(getActivity());
                break;
            default:
                break;
        }
    }

//    @OnClick(R.id.iv_recent)
    public void OnClickClose(){
        mRlRecent.setVisibility(View.GONE);
    }

//    @OnClick(R.id.rl_recent)
//    public void OnClickRecnet(){
//        mPresenter.toRecentComic();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mPresenter.getRecent();
//    }
}