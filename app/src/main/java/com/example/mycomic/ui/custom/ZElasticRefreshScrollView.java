package com.example.mycomic.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.mycomic.R;
import com.example.mycomic.utils.DisplayUtil;
public class ZElasticRefreshScrollView extends ScrollView {

    private RelativeLayout inner;
    private RelativeLayout mMoveView;
    private View mLoadingBottom;
    //private View mListView;
    private View mTopView;
    private float y;
    private Rect normal = new Rect();
    private int height;
    private boolean isRefresh;
    private boolean isAction_UP;
    private RefreshListener listener;
    private Handler mhandler = new Handler();
    private RelativeLayout mLoadingTop;
    private ImageView mLoadingTopImg;
    private TextView mLoadingText;
    public static final int  SCROLL_TO_UP =1;
    public static final int  SCROLL_TO_DOWN =2;
    int oldY= 0;

    public ZElasticRefreshScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ZElasticRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }


    public ZElasticRefreshScrollView(Context context) {
        this(context,null);
    }


    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        height = inner.getMeasuredHeight()-mLoadingBottom.getMeasuredHeight()- DisplayUtil.getScreenHeight(getContext());
        if(y>=height){
            this.scrollTo(0,height);
        }
        //Log.d("zhhr","y="+y+",mTopView.getHeight()="+mTopView.getHeight());
        if(y<mTopView.getHeight()-DisplayUtil.dip2px(getContext(),60)){
            listener.onAlphaActionBar(0f);
        }else if(y<mTopView.getHeight()){
            listener.onAlphaActionBar((float)(y-(mTopView.getHeight()-DisplayUtil.dip2px(getContext(),60)))/DisplayUtil.dip2px(getContext(),60));
        }else{
            listener.onAlphaActionBar(1f);
        }

        if(y - oldY>0){
            listener.onScroll(SCROLL_TO_DOWN);
        }else if(y - oldY<0&&y<height){
            listener.onScroll(SCROLL_TO_UP);
        }
        oldY = y;

        /*if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }*/
    }
    //?????????ScrollView????????????View,????????????inner
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            inner = (RelativeLayout) getChildAt(0);
        }
        mMoveView = (RelativeLayout) inner.getChildAt(0);
        mTopView = inner.getChildAt(1);
        mLoadingTop = (RelativeLayout) mMoveView.getChildAt(0);
        mLoadingText = (TextView) ((LinearLayout) mLoadingTop.getChildAt(0)).getChildAt(1);
        //mListView = mMoveView.getChildAt(2);
        mLoadingBottom = mMoveView.getChildAt(3);
        setOverScrollMode(OVER_SCROLL_NEVER);//??????5.0??????
        mLoadingTopImg = (ImageView) inner.findViewById(R.id.iv_loading_top);
        initAnimation();
    }

    private void initAnimation() {
        mLoadingTopImg.setImageResource(R.drawable.loading_top);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingTopImg.getDrawable();
        animationDrawable.start();
    }

    //??????????????????
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            if(isRefresh&&listener!=null){
                return super.onTouchEvent(ev);
            }
            commOnTouchEvent(ev);
            //???????????????????????????
            if(mMoveView.getTop()>DisplayUtil.dip2px(getContext(),200)&&getScrollY()==0||mMoveView.getBottom()<inner.getMeasuredHeight()&&getScrollY() ==inner.getMeasuredHeight()-mLoadingBottom.getMeasuredHeight()-DisplayUtil.getScreenHeight(getContext())){
                return false;
            }
            return super.onTouchEvent(ev);
        }
    }

    public void commOnTouchEvent(MotionEvent ev) {
        //Log.d("zhhrheight","mLoadingTop = "+mLoadingTop.getMeasuredHeight()+",mLoadingBottom = "+mLoadingBottom.getMeasuredHeight()+",innerHeight="+inner.getMeasuredHeight()+",mListView="+mListView.getMeasuredHeight());
        //Log.d("zhhrheight","getBottomStatusHeight"+ DisplayUtil.getBottomStatusHeight(getContext())+",getScreenHeight="+DisplayUtil.getScreenHeight(getContext())+",getHeight="+getHeight());
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                //Log.d("zhr", "MotionEvent.ACTION_DOWN  y=" + y);
                mLoadingText.setText("????????????");
                listener.onActionDown();
                break;
            case MotionEvent.ACTION_UP:
                listener.onActionUp();
                if (isNeedAnimation()) {
                    // Log.v("mlguitar", "will up and animation");
                    if(mMoveView.getTop()>DisplayUtil.dip2px(getContext(),245)&&listener!=null){
                        RefreshAnimation();
                        mLoadingText.setText("???????????????...");
                        //animation();
                        isRefresh = true;
                        listener.onRefresh();
                    }else{
                        //Log.v("zhhr112233", "getScrollY="+getScrollY()+",height="+height);
                        if(getScrollY()==height&&listener!=null){
                            listener.onLoadMore();
                        }
                        animation();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                //Log.d("zhr", "MotionEvent.ACTION_MOVE  nowY=" + nowY + ";preY=" + preY);
                /**
                 * size=4 ?????? ????????????????????????????????????1/4
                 */
                int deltaY;
                deltaY = (int) Math.sqrt(Math.abs(nowY - preY)*2) ;
                // ??????
                // scrollBy(0, deltaY);

                y = nowY;
                // ????????????????????????????????????????????????????????????????????????
                if (isNeedMove(nowY)) {
                    if (normal.isEmpty()) {
                        // ???????????????????????????
                        normal.set(mMoveView.getLeft(), mMoveView.getTop(),mMoveView.getRight(), mMoveView.getBottom());
                        return;
                    }
                    if (nowY > preY) {
                        //Log.d("zhhr","deltaY="+deltaY);
                        //Log.d("zhhr1122","mMoveView.getTop()="+mMoveView.getTop()+",mMoveView.getBottom="+mMoveView.getBottom()+"height="+height);
                        mMoveView.layout(mMoveView.getLeft(), mMoveView.getTop() + deltaY, mMoveView.getRight(),
                                mMoveView.getBottom() + deltaY);
                    } else if(nowY < preY){
                        //Log.d("zhhr1122","mMoveView.getTop()="+mMoveView.getTop()+",mMoveView.getBottom="+mMoveView.getBottom()+"height="+height);
                        mMoveView.layout(mMoveView.getLeft(), mMoveView.getTop() - deltaY, mMoveView.getRight(),
                                mMoveView.getBottom() - deltaY);
                    }

                    if(mMoveView.getTop()>DisplayUtil.dip2px(getContext(),245)&&listener!=null){
                        mLoadingText.setText("??????????????????");
                    }
                    // ????????????
                }
                break;
            default:
                break;
        }
    }

    // ??????????????????  

    public void animation() {
        // ??????????????????  
        TranslateAnimation ta = new TranslateAnimation(0, 0, mMoveView.getTop()-DisplayUtil.dip2px(getContext(),200), normal.top-DisplayUtil.dip2px(getContext(),200));
       // Log.d("zhhr112233", "inner.getTop()=" + inner.getTop() + ",normal.top=" + normal.top);
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        ta.setDuration(300);
        mMoveView.startAnimation(ta);
        // ?????????????????????????????????  
        mMoveView.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public void RefreshAnimationFinish() {
        // ??????????????????
        TranslateAnimation ta = new TranslateAnimation(0, 0, mMoveView.getTop()-DisplayUtil.dip2px(getContext(),200), normal.top-DisplayUtil.dip2px(getContext(),200));
        // Log.d("zhhr112233", "inner.getTop()=" + inner.getTop() + ",normal.top=" + normal.top);
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        ta.setDuration(300);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.onRefreshFinish();
                mLoadingText.setText("????????????");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLoadingText.setText("????????????");
        mMoveView.startAnimation(ta);
        // ?????????????????????????????????
        mMoveView.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public void RefreshAnimation() {
        // ??????????????????
        TranslateAnimation ta = new TranslateAnimation(0, 0, mMoveView.getTop()-DisplayUtil.dip2px(getContext(),245), normal.top-DisplayUtil.dip2px(getContext(),200));
        // Log.d("zhhr112233", "inner.getTop()=" + inner.getTop() + ",normal.top=" + normal.top);
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        ta.setDuration(300);
        mMoveView.startAnimation(ta);
        // ?????????????????????????????????
        mMoveView.layout(normal.left, normal.top+DisplayUtil.dip2px(getContext(),53), normal.right, normal.bottom+DisplayUtil.dip2px(getContext(),53));
        //normal.setEmpty();
    }

    // ????????????????????????  
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // ????????????????????????  
    public boolean isNeedMove(float nowY) {
        int scrollY = getScrollY();
        return scrollY == 0 && nowY > mTopView.getMeasuredHeight() || scrollY == height;
    }

    public interface ScrollViewListener {
        void onScrollChanged(ZElasticRefreshScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    /**
     * ??????????????????
     * @param isRefresh
     */
    public void setRefreshing(boolean isRefresh){
        if(this.isRefresh!=isRefresh){
            this.isRefresh = isRefresh;
            if(isRefresh == false){
                RefreshAnimationFinish();
            }
        }
    }

    public boolean isRefreshing(){
        return isRefresh;
    }

    public interface RefreshListener{
        void onActionDown();
        void onActionUp();
        void onRefresh();
        void onRefreshFinish();
        void onLoadMore();
        void onScroll(int y);
        void onAlphaActionBar(float a);
    }

    public void setRefreshListener(RefreshListener listener){
        this.listener = listener;
    }

}
