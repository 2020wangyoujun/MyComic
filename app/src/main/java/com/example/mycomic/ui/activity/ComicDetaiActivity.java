package com.example.mycomic.ui.activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycomic.R;
import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.presenter.ComicDetailPresenter;
import com.example.mycomic.ui.activity.base.BaseActivity;
import com.example.mycomic.ui.custom.DetailFloatLinearLayout;
import com.example.mycomic.ui.custom.DetailScrollView;
import com.example.mycomic.ui.custom.IndexItemView;
import com.example.mycomic.ui.view.IDetailView;
import com.example.mycomic.utils.DisplayUtil;
import com.example.mycomic.utils.IntentUtil;
import com.example.mycomic.utils.LogUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ComicDetaiActivity extends BaseActivity<ComicDetailPresenter> implements IDetailView<Comic>, IndexItemView.onItemClickLinstener{

    ImageView mHeaderView;
    DetailScrollView mScrollView;
    TextView mTitle;
    LinearLayout mText;
    ImageView mHeaderViewBg;
    TextView mAuthorTag;
    TextView mCollects;
    TextView mDescribe;
    TextView mPopularity;
    TextView mStatus;
    TextView mUpdate;
    TextView mPoint;
    RelativeLayout mDetail;
    TextView mTab;
    ImageView mOrder;
    TextView mActionBarTitle;
    ImageView mOrder2;
    Button mRead;
    ImageView mLoading;
    RelativeLayout mRLloading;
    TextView mLoadingText;
    ImageView mReload;
    TextView mLoadingTitle;
    LinearLayout mIndex;
    ImageView mCollect;
    TextView mIsCollect;
    DetailFloatLinearLayout mFloatButtom;
//    //add start by ad
//    @Bind(R.id.layout1_banner)
    LinearLayout mAdBanner;
    LinearLayout llCollect;
    //ZAdComponent banner;

    ImageView ivBack,ivBackColor,ivDownload;


    private float mScale = 1.0f;
    private float Dy = 0;
    private Rect normal = new Rect();
    private int mCurrent;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new ComicDetailPresenter(this,this,intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_comic_detail;
    }

    @Override
    protected void initView() {
        mLoadingTitle=findViewById(R.id.tv_loading_title);
        mLoadingTitle.setText(getIntent().getStringExtra(Constants.COMIC_TITLE));
        mScrollView=findViewById(R.id.sv_comic);
        mScrollView.setScaleTopListener(new MyScaleTopListener());

        mLoading=findViewById(R.id.iv_loading);
        mLoading.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoading.getDrawable();
        animationDrawable.start();
        mPresenter.getDetail();
        mFloatButtom=findViewById(R.id.ll_floatbottom);
        mFloatButtom.setOnFloatBottomClickListener(new DetailFloatLinearLayout.FloatButtomOnclickListener() {
            @Override
            public void onClickLocation(View view) {
                mScrollView.ScrollToPosition(mCurrent);
            }

            @Override
            public void onClickScroll(View view) {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        //add
        /*if(Constants.isAD){
            if(banner==null){
                banner = ZAdSdk.getInstance().createAd(this, ZAdType.BANNER, "1001");
                mAdBanner.addView(banner.getContentView()); // 添加到父视图里
            }
            ZAdSdk.getInstance().getLoader().loadAd(banner);
        }*/

        mRLloading=findViewById(R.id.rl_loading);
        mReload=findViewById(R.id.iv_error);
        mLoadingText=findViewById(R.id.tv_loading);
        mHeaderView=findViewById(R.id.iv_image);
        mHeaderViewBg=findViewById(R.id.iv_image_bg);
        mActionBarTitle=findViewById(R.id.tv_actionbar_title);
        mTitle=findViewById(R.id.tv_title);
        mAuthorTag=findViewById(R.id.tv_author_tag);
        mCollects=findViewById(R.id.tv_collects);
        mDescribe=findViewById(R.id.tv_describe);
        mStatus=findViewById(R.id.tv_status);
        mPopularity=findViewById(R.id.tv_popularity);
        mUpdate=findViewById(R.id.tv_update);
        mPoint=findViewById(R.id.tv_point);
        mText=findViewById(R.id.ll_text);
        mRead=findViewById(R.id.btn_read);
        mIndex=findViewById(R.id.ll_index);
        mOrder=findViewById(R.id.iv_order);
        mOrder2=findViewById(R.id.iv_oreder2);
        mCollect=findViewById(R.id.iv_collect);
        mIsCollect=findViewById(R.id.tv_collect);
        mDetail=findViewById(R.id.ll_detail);
        mTab=findViewById(R.id.tv_tab);
        mAdBanner=findViewById(R.id.layout1_banner);

        ivBack=findViewById(R.id.iv_back);
        ivBackColor=findViewById(R.id.iv_back_color);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnFinish(v);
            }
        });

        ivBackColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnFinish(v);
            }
        });

        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderList((ImageView) v);
            }
        });

        mOrder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderList((ImageView) v);
            }
        });

        mRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartRead(v);
            }
        });
        mReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload(v);
            }
        });

        llCollect=findViewById(R.id.ll_collect);
        llCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectComic(v);
            }
        });

        ivDownload=findViewById(R.id.iv_download);
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSelectDownloadActivity(v);
            }
        });
    }

    @Override
    protected void onResume() {
        mPresenter.getCurrentChapters();
        super.onResume();
    }

    @Override
    public void getDataFinish() {

    }


    @Override
    public void showErrorView(String throwable) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        mLoadingText.setText(throwable);
    }

    @Override
    public void fillData(final Comic comic) {
        mRLloading.setVisibility(View.GONE);
        Glide.with(this)
                .load(comic.getCover())
                .placeholder(R.mipmap.pic_default)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(RequestOptions.centerCropTransform())
                .into(mHeaderView);
        Glide.with(this)
                .load(comic.getCover())
                .placeholder(R.mipmap.pic_default)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .transform(new BlurTransformation(25, 10))
//                .bitmapTransform(new BlurTransformation(this, 10))
                .into(mHeaderViewBg);
        mActionBarTitle.setText(comic.getTitle());
        mTitle.setText(comic.getTitle());
        mAuthorTag.setText(comic.getAuthor()+comic.getTags().toString());
        mCollects.setText(comic.getCollections());
        mDescribe.setText(comic.getDescribe());
        mStatus.setText(comic.getStatus());
        mPopularity.setText(comic.getPopularity());
        mUpdate.setText(comic.getUpdates());
        mPoint.setText(comic.getPoint());
        normal.set(mText.getLeft(),mText.getTop(),getMobileWidth(),mText.getBottom());
        mCurrent = mPresenter.getmComic().getCurrentChapter();
        if(mCurrent>0){
            mRead.setText("续看第"+mCurrent+"话");
        }
        for(int i=0;i<comic.getChapters().size();i++){
            IndexItemView indexItemView = new IndexItemView(this,comic.getChapters().get(i),i);
            indexItemView.setListener(this);
            mIndex.addView(indexItemView);
        }
        mScrollView.setInnerHeight();
        setCollect(comic.getIsCollected());
        setCurrent(mCurrent+1);
    }

    @Override
    public void OrderData(int ResId) {
        mOrder.setImageResource(ResId);
        mOrder2.setImageResource(ResId);
    }

    @Override
    public void setCollect(boolean isCollect) {
        if(isCollect){
            mCollect.setImageResource(R.mipmap.collect_selet);
            mIsCollect.setText("已收藏");
        }else{
            mCollect.setImageResource(R.mipmap.collect);
            mIsCollect.setText("收藏");
        }

    }

    @Override
    public void setCurrent(int current) {
        if(mIndex.getChildCount()!=0){
            try{
                if(!mPresenter.isOrder()){
                    if(mCurrent-1>=0){
                        ((IndexItemView)mIndex.getChildAt(mCurrent-1)).setCurrentColor(false);
                    }
                    if(current-1>=0){
                        ((IndexItemView)mIndex.getChildAt(current-1)).setCurrentColor(true);
                    }
                }else{
                    if(mPresenter.getmComic().getChapters().size()-mCurrent>=0&&mCurrent!=0){
                        ((IndexItemView)mIndex.getChildAt(mPresenter.getmComic().getChapters().size()-mCurrent)).setCurrentColor(false);
                    }
                    if(mPresenter.getmComic().getChapters().size()-current>=0){
                        ((IndexItemView)mIndex.getChildAt(mPresenter.getmComic().getChapters().size()-current)).setCurrentColor(true);
                    }
                }
            }catch (Exception e){
                LogUtil.e(e.toString());
            }
            mCurrent = current;
            mRead.setText("续看第"+mCurrent+"话");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(mPresenter.isOrder()){
            position = mPresenter.getmComic().getChapters().size()-position-1;
            Log.d("ComicDetailActivity","position="+position);
        }
        IntentUtil.ToComicChapter(ComicDetaiActivity.this, position, mPresenter.getmComic());

    }


    public class MyScaleTopListener implements DetailScrollView.ScaleTopListener {
        @Override
        public void isScale(float y) {
            int height = DisplayUtil.dip2px(ComicDetaiActivity.this,200);
            mScale = y/ height;
            float width = getMobileWidth()*mScale;
            float dx= (width - getMobileWidth())/2;
            mHeaderView.layout((int) (0-dx),0, (int) (getMobileWidth()+dx), (int) y);
            Dy = y - height;
            mText.layout(normal.left,(int)(normal.top+Dy),normal.right,(int)(normal.bottom+Dy));
            //Log.d("DetailActivity","Dy="+Dy+",normal="+normal.toString());
        }

        @Override
        public void isFinished() {
            //Log.d("DetailScrollView","Dy="+Dy);
            Interpolator in = new DecelerateInterpolator();
            ScaleAnimation ta = new ScaleAnimation(mScale, 1.0f, mScale, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            //第一个参数fromX为动画起始时 X坐标上的伸缩尺寸
            //第二个参数toX为动画结束时 X坐标上的伸缩尺寸
            //第三个参数fromY为动画起始时Y坐标上的伸缩尺寸
            //第四个参数toY为动画结束时Y坐标上的伸缩尺寸
            //第五个参数pivotXType为动画在X轴相对于物件位置类型
            //第六个参数pivotXValue为动画相对于物件的X坐标的开始位置
            //第七个参数pivotXType为动画在Y轴相对于物件位置类型
            //第八个参数pivotYValue为动画相对于物件的Y坐标的开始位置
            ta.setInterpolator(in);
            ta.setDuration(300);
            mHeaderView.startAnimation(ta);
            mHeaderView.layout(0,0,getMobileWidth(), DisplayUtil.dip2px(ComicDetaiActivity.this,200));
            //设置文字活动的动画
            TranslateAnimation trans = new TranslateAnimation(0, 0 , Dy, 0 );
            trans.setInterpolator(in);
            trans.setDuration(300);
            mText.startAnimation(trans);
            mText.layout(normal.left,normal.top,normal.right,normal.bottom);
            //统统重置
            mScale = 1.0f;
            Dy = 0;
        }

        @Override
        public void isBlurTransform(float y) {
            mHeaderViewBg.setAlpha(1-y);
        }

        @Override
        public void isShowTab(int a) {
            setTab(a);
        }
    }

    public void setTab(int a){
        switch (a){
            case DetailScrollView.SHOW_CHAPER_TAB:
                if(mDetail.getVisibility() == View.GONE){
                    mDetail.setVisibility(View.VISIBLE);
                }else {
                    mTab.setText("详情");
                    mOrder.setVisibility(View.GONE);
                }
                break;
            case DetailScrollView.SHOW_DETAIL_TAB:
                mTab.setText("目录");
                mOrder.setVisibility(View.VISIBLE);
                break;
            case DetailScrollView.SHOW_ACTIONBAR_TITLE:
                if(mDetail.getVisibility() == View.VISIBLE) {
                    mOrder.setVisibility(View.GONE);
                    mDetail.setVisibility(View.GONE);
                    mTab.setText("详情");
                }
                if(mActionBarTitle.getVisibility() == View.GONE){
                    mActionBarTitle.setVisibility(View.VISIBLE);
                    AnimationSet animationSet = new AnimationSet(true);
                    TranslateAnimation trans = new TranslateAnimation(0, 0 , DisplayUtil.dip2px(getApplicationContext(),12), 0 );
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                    animationSet.addAnimation(trans);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.setDuration(200);
                    mActionBarTitle.startAnimation(animationSet);
                }
                break;
            case DetailScrollView.SHOW_NONE:
                if(mDetail.getVisibility() == View.VISIBLE){
                    mTab.setText("详情");
                    mOrder.setVisibility(View.GONE);
                    mDetail.setVisibility(View.GONE);
                }
                if(mActionBarTitle.getVisibility() == View.VISIBLE){
                    mActionBarTitle.setVisibility(View.GONE);
                    AnimationSet animationSet = new AnimationSet(true);
                    TranslateAnimation trans = new TranslateAnimation(0, 0 , 0, DisplayUtil.dip2px(getApplicationContext(),12) );
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
                    animationSet.addAnimation(trans);
                    animationSet.addAnimation(alphaAnimation);
                    animationSet.setDuration(200);
                    mActionBarTitle.startAnimation(animationSet);
                }
                break;
        }
    }

    @Override
    public void ShowToast(String toast) {
        showToast(toast);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdBanner.removeAllViews();
    }

    //点击事件
//    @OnClick({R.id.iv_back_color, R.id.iv_back})
    public void OnFinish(View view){
        finish();
    }

//    @OnClick({ R.id.iv_order,R.id.iv_oreder2 })
    public void OrderList(ImageView Order) {
        mPresenter.setOrder(!mPresenter.isOrder());
        if(!mPresenter.isOrder()){
            mOrder2.setImageResource(R.mipmap.zhengxu);
            mOrder.setImageResource(R.mipmap.zhengxu);
        }else{
            mOrder2.setImageResource(R.mipmap.daoxu);
            mOrder.setImageResource(R.mipmap.daoxu);
        }
        mPresenter.orderIndex(mIndex);
    }

//    @OnClick(R.id.btn_read)
    public void StartRead(View view){
        if(mCurrent == 0){
            IntentUtil.ToComicChapter(this,0,mPresenter.getmComic());
        }else{
            IntentUtil.ToComicChapter(this,mCurrent-1,mPresenter.getmComic());
        }

    }

//    @OnClick(R.id.iv_error)
    public void reload(View view){
        mPresenter.getDetail();
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
        mLoadingText.setText("正在重新加载，请稍后");
    }

//    @OnClick(R.id.ll_collect)
    public void selectComic(View view){
        mPresenter.collectComic(!mPresenter.getmComic().getIsCollected());
    }

//    @OnClick(R.id.iv_download)
    public void toSelectDownloadActivity(View view){
        IntentUtil.ToSelectDownload(this,mPresenter.getmComic());
    }
}
