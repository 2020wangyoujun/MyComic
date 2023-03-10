package com.example.mycomic.ui.activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.mycomic.R;
import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.PreloadChapters;
import com.example.mycomic.presenter.ComicChapterPresenter;
import com.example.mycomic.ui.activity.base.BaseActivity;
import com.example.mycomic.ui.adapter.ChapterRecycleAdapter;
import com.example.mycomic.ui.adapter.ChapterViewpagerAdapter;
import com.example.mycomic.ui.custom.ComicReaderViewpager;
import com.example.mycomic.ui.custom.IndexLayout;
import com.example.mycomic.ui.custom.ReaderMenuLayout;
import com.example.mycomic.ui.custom.SwitchRelativeLayout;
import com.example.mycomic.ui.custom.ZBubbleSeekBar;
import com.example.mycomic.ui.custom.ZoomRecyclerView;
import com.example.mycomic.ui.view.IChapterView;
import com.example.mycomic.utils.IntentUtil;
import com.example.mycomic.utils.LogUtil;

public class ComicChapterActivity extends BaseActivity<ComicChapterPresenter> implements IChapterView<PreloadChapters> {

    ComicReaderViewpager mViewpager;
//    @Bind(R.id.rl_top)
//    RelativeLayout mTop;
//    @Bind(R.id.rl_bottom)
//    RelativeLayout mBottom;
    ReaderMenuLayout menuLayout;
    ImageView mBack;
    ImageView mBackColor;
    TextView mTitle;
    ZBubbleSeekBar mSeekbar;
    ChapterViewpagerAdapter mAdapter;
    ImageView mLoading;
    RelativeLayout mRLloading;
    TextView mLoadingText;
    ImageView mReload;
    TextView mLoadingTitle;
    SwitchRelativeLayout mSwitchModel;
    Button mNormal;
    Button mJcomic;
    Button mDown;
//    @Bind(R.id.rl_switch_night)
//    SwitchNightRelativeLayout mSwitchNight;
//    @Bind(R.id.rl_index)
    IndexLayout mIndex;
    ZoomRecyclerView mRecycleView;
    ImageView ivIndex,ivSetting,ivSwitch,ivLight,ivDownload;
    Button ivNomalMode,ivJcomicMode,ivDownMode;
    private ChapterRecycleAdapter mVerticalAdapter;
    private LinearLayoutManager manager;
    int mCurrentPosition = 0;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new ComicChapterPresenter(this,this);
        mPresenter.init((Comic)intent.getSerializableExtra(Constants.COMIC),intent.getIntExtra(Constants.COMIC_CHAPERS,0));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chapter;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mRLloading.setVisibility(View.VISIBLE);
        mLoadingText.setText("????????????,?????????...");
        initPresenter(intent);
        initView();
    }

    @Override
    protected void initView() {
        setNavigation();
        mLoadingText=findViewById(R.id.tv_loading);

        mLoading=findViewById(R.id.iv_loading);
        mLoading.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoading.getDrawable();
        animationDrawable.start();
        //?????????????????????
        mAdapter = new ChapterViewpagerAdapter(this);
        mAdapter.setDirection(mPresenter.getmDirect());
        mViewpager=findViewById(R.id.vp_chapters);
        mViewpager.setOffscreenPageLimit(4);
        mAdapter.setListener(new ChapterViewpagerAdapter.OnceClickListener() {
            @Override
            public void onClick(View view, float x, float y) {
                mPresenter.clickScreen(x,y);
            }
        });
        mViewpager.setAdapter(mAdapter);
        menuLayout=findViewById(R.id.rl_menu);
        mSwitchModel=findViewById(R.id.rl_switch_model);
        mIndex=findViewById(R.id.rl_index);
        mSeekbar=findViewById(R.id.sb_seekbar);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(menuLayout.isShow()){
                    menuLayout.setVisibility(View.GONE);
                }
                if(mSwitchModel.isShow()){
                    mSwitchModel.setVisibility(View.GONE);
                }
                if(mIndex.isShow()){
                    mIndex.setVisibility(View.GONE);
                }
                if(mAdapter.getDirection() == Constants.LEFT_TO_RIGHT){
                    mSeekbar.setProgress(position-mPresenter.getmPreloadChapters().getPrelist().size()+1);
                }else{
                    mSeekbar.setProgress(position-mPresenter.getmPreloadChapters().getNextlist().size()+1);
                }

                mPresenter.loadMoreData(position,mAdapter.getDirection(),0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                   /* case  ViewPager.SCROLL_STATE_DRAGGING:
                        mIsScrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        mIsScrolled = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if(!mIsScrolled){
                            if(!isLoadingdata){
                                //mPresenter.loadMoreData(comic_id,comic_chapters,comic_postion,mAdapter.getDirection());
                                isLoadingdata = true;
                            }
                        }
                        mIsScrolled = true;
                        break;*/
                }

            }
        });
        //?????????????????????
        mVerticalAdapter = new ChapterRecycleAdapter(this,R.layout.item_comic_reader);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRecycleView=findViewById(R.id.rv_chapters);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(mVerticalAdapter);
       /* mVerticalAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                mPresenter.clickScreen(0.5f,0.5f);
            }
        });*/
        mRecycleView.setTouchListener(new ZoomRecyclerView.OnTouchListener() {
            @Override
            public void clickScreen(float x, float y) {
                //?????????????????????????????????????????????????????????????????????????????????
                mPresenter.clickScreen(0.5f,0.5f);
            }
        });
        mRecycleView.setEnableScale(true);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View topView = recyclerView.getChildAt(0);          //????????????????????????view
                int lastOffset = topView.getTop();
                mPresenter.setLoadingDy(lastOffset);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //???????????????layoutManager?????????LinearLayoutManager
                // ??????LinearLayoutManager??????????????????????????????????????????view???????????????
                if(menuLayout.isShow()){
                    menuLayout.setVisibility(View.GONE);
                }
                if(mSwitchModel.isShow()){
                    mSwitchModel.setVisibility(View.GONE);
                }
                if(mIndex.isShow()){
                    mIndex.setVisibility(View.GONE);
                }
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //????????????????????????view?????????
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    if(firstItemPosition!=mCurrentPosition){
                        mCurrentPosition = firstItemPosition;
                        mSeekbar.setProgress(mCurrentPosition-mPresenter.getmPreloadChapters().getPrelist().size()+1);
                        mPresenter.loadMoreData(mCurrentPosition,Constants.UP_TO_DOWN,0);
                        if(firstItemPosition == 0||lastItemPosition == mPresenter.getmPreloadChapters().getDataSize()-1){
                            showToast("?????????");
                        }
                    }
                }
            }
        });

        //SeekBar?????????
        mSeekbar.setOnProgressChangedListener(new ZBubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                if(mAdapter.getDirection() == Constants.RIGHT_TO_LEFT){
                    mViewpager.setCurrentItem(progress+mPresenter.getmPreloadChapters().getNextlist().size()-1);
                }else if (mAdapter.getDirection() == Constants.LEFT_TO_RIGHT){
                    mViewpager.setCurrentItem(progress+mPresenter.getmPreloadChapters().getPrelist().size()-1);
                }else{
                    mRecycleView.smoothScrollToPosition(progress+mPresenter.getmPreloadChapters().getPrelist().size()-1);
                }
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
        mRLloading=findViewById(R.id.rl_loading);
        mRLloading.setOnClickListener(null);
        //???????????????????????????
        mLoadingTitle=findViewById(R.id.tv_loading_title);
        try{//?????????????????????????????????
            mLoadingTitle.setText(mPresenter.getComic_chapter_title().get(mPresenter.getComic_chapters()));
        }catch (Exception e){
            LogUtil.e(e.toString());
        }
        mTitle=findViewById(R.id.tv_title);
        /*if(Constants.isAD){
            mPresenter.loadDataforAd();
        }else{*/
            mPresenter.loadData();
        /*}*/

        mNormal=findViewById(R.id.iv_normal_model);
        mJcomic=findViewById(R.id.iv_j_comic_model);
        mDown=findViewById(R.id.iv_down_model);
        initReaderModule(mPresenter.getmDirect());

        mReload=findViewById(R.id.iv_error);
        mBack=findViewById(R.id.iv_back);
        mBackColor=findViewById(R.id.iv_back_color);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload(v);
            }
        });
        ivIndex=findViewById(R.id.iv_index);
        ivIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toIndex(v);
            }
        });
        ivSetting=findViewById(R.id.iv_setting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSwitch(v);
            }
        });
        ivSwitch=findViewById(R.id.iv_switch);
        ivSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHorizontal(v);
            }
        });
        ivLight=findViewById(R.id.iv_light);
        ivLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSwitchNight(v);
            }
        });
        ivDownload=findViewById(R.id.iv_download);
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDownload(v);
            }
        });
        ivNomalMode=findViewById(R.id.iv_normal_model);
        ivNomalMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchToLeftToRight(v);
            }
        });
        ivJcomicMode=findViewById(R.id.iv_j_comic_model);
        ivJcomicMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchToRightToLeft(v);
            }
        });
        ivDownMode=findViewById(R.id.iv_j_comic_model);
        ivDownMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchToUpToDown(v);
            }
        });
    }

    /**
     * ??????setNavigation()
     */
    private void setNavigation() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * ????????????????????????????????????
     */
    private void initReaderModule(int mDirect) {
        if(mDirect == Constants.LEFT_TO_RIGHT){
            mSeekbar.setSeekBarColor(true);
            mNormal.setBackgroundResource(R.drawable.normal_model_press);
            mJcomic.setBackgroundResource(R.drawable.btn_switchmodel_j_comic);
            mDown.setBackgroundResource(R.drawable.btn_switchmodel_down);
        }else if(mDirect == Constants.RIGHT_TO_LEFT){
            mSeekbar.setSeekBarColor(false);
            mNormal.setBackgroundResource(R.drawable.btn_switchmodel_normal);
            mJcomic.setBackgroundResource(R.drawable.j_comic_model_press);
            mDown.setBackgroundResource(R.drawable.btn_switchmodel_down);
        }else{
            mSeekbar.setSeekBarColor(true);
            mNormal.setBackgroundResource(R.drawable.btn_switchmodel_normal);
            mJcomic.setBackgroundResource(R.drawable.btn_switchmodel_j_comic);
            mDown.setBackgroundResource(R.drawable.down_model_press);
        }

    }

    @Override
    public void fillData(PreloadChapters datas) {
        mIndex.setData(mPresenter.getmComic(),mPresenter.getComic_chapters());
        mPresenter.setmPreloadChapters(datas);
        if(mPresenter.getmDirect()==Constants.UP_TO_DOWN){
            mVerticalAdapter.setDatas(datas);
            mRecycleView.setVisibility(View.VISIBLE);
            mViewpager.setVisibility(View.GONE);
            mRecycleView.scrollToPosition(datas.getPrelist().size()+mPresenter.getCurrentPage());
            mCurrentPosition = datas.getPrelist().size();
            mSeekbar.setProgress(mPresenter.getCurrentPage()+1);
        }else{
            mRecycleView.setVisibility(View.GONE);
            mViewpager.setVisibility(View.VISIBLE);
            mAdapter.setDatas(datas);
            if(mAdapter.getDirection() == Constants.RIGHT_TO_LEFT){
                mViewpager.setCurrentItem(datas.getNextlist().size()+datas.getNowlist().size()-1-mPresenter.getCurrentPage(),false);//??????????????????
            }else{
                mViewpager.setCurrentItem(datas.getPrelist().size()+mPresenter.getCurrentPage(),false);
            }
        }
        mSeekbar.setmMax(datas.getNowlist().size());
    }

    @Override
    public void setTitle(String name) {
        mTitle.setText(name);
    }

    @Override
    public void SwitchSkin() {
//        switchModel();
    }

    @Override
    public void setSwitchNightVisible(int visible, boolean isNight) {
//        mSwitchNight.setVisibility(visible,isNight);
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }
    //??????????????????
    @Override
    public void getDataFinish() {
        mRLloading.setVisibility(View.GONE);
    }

    //??????????????????
    @Override
    public void showErrorView(String error) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        mLoadingText.setText(error);
    }


    @Override
    public void showMenu() {
        if(!menuLayout.isShow()){
            menuLayout.setVisibility(View.VISIBLE);
        }else{
            menuLayout.setVisibility(View.GONE);
        }
        if(mIndex.isShow()){
            mIndex.setVisibility(View.GONE);
        }
        if(mSwitchModel.isShow()){
            mSwitchModel.setVisibility(View.GONE);
        }
    }


    @Override
    public void nextChapter(PreloadChapters data, int mPosition,int offset) {
        mIndex.setData(mPresenter.getmComic(),mPresenter.getComic_chapters());
        if(mPresenter.getmDirect()==Constants.UP_TO_DOWN){
            mVerticalAdapter.setDatas(data);
            manager.scrollToPositionWithOffset(mPosition,offset);
            //???????????????????????????progress?????????????????????????????????viewpager???onPageSelected??????
            mSeekbar.setProgress(mPosition-data.getPreSize());
        }else{
            mAdapter.setDatas(data);
            mViewpager.setCurrentItem(mPosition,false);
            //????????????????????????????????????????????????Progress???????adapter???LIST????????????????????????????????????????????????????????????onPageSelected????????????????????????Progress
            if(mPresenter.getComic_chapters()==1){
                if(mAdapter.getDirection()==Constants.LEFT_TO_RIGHT){
                    mSeekbar.setProgress(1);
                }else{
                    mSeekbar.setProgress(data.getNowlist().size());
                }
            }
        }
        mSeekbar.setmMax(data.getNowlist().size());
    }

    @Override
    public void preChapter(PreloadChapters data, int mPosition,int offset) {
        mIndex.setData(mPresenter.getmComic(),mPresenter.getComic_chapters());
        if(mPresenter.getmDirect()==Constants.UP_TO_DOWN){
            mVerticalAdapter.setDatas(data);
            manager.scrollToPositionWithOffset(mPosition,offset);
            //???????????????????????????progress?????????????????????????????????viewpager???onPageSelected??????
            mSeekbar.setProgress(mPosition-data.getPreSize());
        }else {
            mAdapter.setDatas(data);
            mViewpager.setCurrentItem(mPosition,false);
            //????????????????????????????????????????????????Progress???????adapter???LIST????????????????????????????????????????????????????????????onPageSelected????????????????????????Progress
            if(mPresenter.getComic_chapters()==0){
                if(mAdapter.getDirection()==Constants.LEFT_TO_RIGHT){
                    mSeekbar.setProgress(data.getNowlist().size());
                }else{
                    mSeekbar.setProgress(1);
                }
            }
        }
        mSeekbar.setmMax(data.getNowlist().size());
    }

    @Override
    public void SwitchModel(int mDirect) {
        if(mPresenter.getmDirect()!=mDirect){
            //?????????????????????
            if(mDirect == Constants.UP_TO_DOWN){
                mRecycleView.setVisibility(View.VISIBLE);
                mViewpager.setVisibility(View.GONE);
                mAdapter.clearList();
                int nowposition = mViewpager.getCurrentItem();
                mVerticalAdapter.setDatas(mPresenter.getmPreloadChapters());
                //???????????????????????????
                if(mPresenter.getmDirect()==Constants.LEFT_TO_RIGHT){
                    mRecycleView.scrollToPosition(nowposition);
                    mCurrentPosition = nowposition;
                }else if(mPresenter.getmDirect()==Constants.RIGHT_TO_LEFT){ //???????????????????????????
                    mRecycleView.scrollToPosition(mPresenter.getmPreloadChapters().getDataSize()-nowposition-1);
                    mCurrentPosition = mPresenter.getmPreloadChapters().getDataSize()-nowposition-1;
                }
                mSeekbar.setProgress(mCurrentPosition-mPresenter.getmPreloadChapters().getPreSize()+1);
            }else{
                //?????????????????????
                mRecycleView.setVisibility(View.GONE);
                mViewpager.setVisibility(View.VISIBLE);
                mVerticalAdapter.clearList();
                int nowposition;
                if(mPresenter.getmDirect()==Constants.UP_TO_DOWN){
                    if(mDirect==Constants.RIGHT_TO_LEFT){
                        nowposition = mCurrentPosition+1;
                    }else{
                        nowposition =mPresenter.getmPreloadChapters().getDataSize()-mCurrentPosition-2;
                    }
                }else{
                    nowposition = mViewpager.getCurrentItem();
                }
                //????????????new ??????adapter ????????????????????????????????????????????????????????????????????????
                mAdapter = new ChapterViewpagerAdapter(this,mPresenter.getmPreloadChapters(),mDirect);
                mAdapter.setListener(new ChapterViewpagerAdapter.OnceClickListener() {
                    @Override
                    public void onClick(View view, float x, float y) {
                        mPresenter.clickScreen(x,y);
                    }
                });
                mViewpager.setAdapter(mAdapter);
                mViewpager.setCurrentItem(mPresenter.getmPreloadChapters().getDataSize()-nowposition-1,false);
            }
            mSwitchModel.setVisibility(View.GONE,mDirect);
            mPresenter.setmDirect(mDirect);
            initReaderModule(mDirect);
        }
    }

    @Override
    public void prePage() {
        if(mPresenter.getmDirect()==Constants.UP_TO_DOWN){

        }else{
            int postion = mViewpager.getCurrentItem()-1;
            if(postion>=0){
                mViewpager.setCurrentItem(postion);
            }else{
                showToast("?????????");
            }
        }

    }

    @Override
    public void nextPage() {
        if(mPresenter.getmDirect()==Constants.UP_TO_DOWN){

        }else {
            int postion = mViewpager.getCurrentItem()+1;
            if(postion<mAdapter.getCount()){
                mViewpager.setCurrentItem(postion);
            }else{
                showToast("?????????");
            }
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.interruptThread();
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

//    @OnClick({R.id.iv_back,R.id.iv_back_color})
    public void finish(View view){
        this.finish();
    }

//    @OnClick(R.id.iv_error)
    public void reload(View view){
        mPresenter.loadData();
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
        mLoadingText.setText("??????????????????????????????");
    }

//    @OnClick(R.id.iv_index)
    public void toIndex(View view){
        //IntentUtil.ToIndex(ComicChapterActivity.this,mPresenter.getmComic());
        menuLayout.setVisibility(View.GONE);
        mIndex.setVisibility(View.VISIBLE);
    }

//    @OnClick(R.id.iv_setting)
    public void toSwitch(View view){
        menuLayout.setVisibility(View.GONE);
        if(!mSwitchModel.isShow()){
            mSwitchModel.setVisibility(View.VISIBLE);
        }else {
            mSwitchModel.setVisibility(View.GONE);
        }
        if(mIndex.isShow()){
            mIndex.setVisibility(View.GONE);
        }
    }
//    @OnClick(R.id.iv_switch)
    public void toHorizontal(View view){
        showToast("?????????");
    }

//    @OnClick(R.id.iv_light)
    public void toSwitchNight(View view){
       mPresenter.switchNight(isNight);
    }

//    @OnClick(R.id.iv_download)
    public void toDownload(View view){
        IntentUtil.ToSelectDownload(this,mPresenter.getmComic());
    }

//    @OnClick(R.id.iv_normal_model)
    public void SwitchToLeftToRight(View view){
        mPresenter.setReaderModuel(Constants.LEFT_TO_RIGHT);
    }

//    @OnClick(R.id.iv_j_comic_model)
    public void SwitchToRightToLeft(View view){
        mPresenter.setReaderModuel(Constants.RIGHT_TO_LEFT);
    }

//    @OnClick(R.id.iv_down_model)
    public void SwitchToUpToDown(View view){
        mPresenter.setReaderModuel(Constants.UP_TO_DOWN);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//???????????????
        if ((keyCode == KeyEvent.KEYCODE_BACK)&&(mIndex.isShow()||menuLayout.isShow()||mSwitchModel.isShow())) {
            if(menuLayout.isShow()){
                menuLayout.setVisibility(View.GONE);
            }
            if(mSwitchModel.isShow()){
                mSwitchModel.setVisibility(View.GONE);
            }
            if(mIndex.isShow()){
                mIndex.setVisibility(View.GONE);
            }

            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
