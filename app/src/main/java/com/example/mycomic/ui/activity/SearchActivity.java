package com.example.mycomic.ui.activity;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.presenter.SearchPresenter;
import com.example.mycomic.ui.activity.base.BaseActivity;
import com.example.mycomic.ui.adapter.SearchDynamicAdapter;
import com.example.mycomic.ui.adapter.SearchHistoryAdapter;
import com.example.mycomic.ui.adapter.SearchResultAdapter;
import com.example.mycomic.ui.adapter.SearchTopAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.custom.NoScrollGridLayoutManager;
import com.example.mycomic.ui.custom.NoScrollStaggeredGridLayoutManager;
import com.example.mycomic.ui.view.ISearchView;
import com.example.mycomic.utils.IntentUtil;
import com.example.mycomic.utils.TextUtils;

import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView<List<Comic>> {
    EditText mSearchText;
    RecyclerView mDynamicRecycle;
    ImageView mClearText;
    RecyclerView mResultRecycle;
    RecyclerView mTopRecycle;
    RecyclerView mHistoryRecycle;
    RelativeLayout mNormal;
    TextView mError;
    SearchDynamicAdapter mDynaicAdapter;
    SearchResultAdapter mResultAdapter;
    SearchTopAdapter mTopAdapter;
    SearchHistoryAdapter mHistoryAdapter;
    TextView tvCancel;
    ImageView ivClearHistory;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new SearchPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            initStatusBar(false);
//        }
        initStatusBar(false);
        mNormal=findViewById(R.id.rl_normal);
        mError=findViewById(R.id.tv_error);
        tvCancel=findViewById(R.id.tv_cancel);
        ivClearHistory=findViewById(R.id.iv_clear_history);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mDynaicAdapter = new SearchDynamicAdapter(this,R.layout.item_dynamic_search);
        mDynamicRecycle=findViewById(R.id.iv_dynamic_recycle);
        mDynamicRecycle.setLayoutManager(manager);
        mDynamicRecycle.setAdapter(mDynaicAdapter);

        mResultAdapter = new SearchResultAdapter(this,R.layout.item_search_result);
        manager = new LinearLayoutManager(this);
        mResultRecycle=findViewById(R.id.iv_result_recycle);
        mResultRecycle.setLayoutManager(manager);
        mResultRecycle.setAdapter(mResultAdapter);

        NoScrollStaggeredGridLayoutManager staggeredGridLayoutManager = new NoScrollStaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL);
        staggeredGridLayoutManager.setScrollEnabled(false);
        mTopAdapter = new SearchTopAdapter(this,R.layout.item_top_search);
        mTopRecycle=findViewById(R.id.iv_top_search_recycle);
        mTopRecycle.setLayoutManager(staggeredGridLayoutManager);
        mTopRecycle.setAdapter(mTopAdapter);

        mHistoryAdapter = new SearchHistoryAdapter(this,R.layout.item_history_search);
        NoScrollGridLayoutManager gridLayoutManager = new NoScrollGridLayoutManager(this,1);
        gridLayoutManager.setScrollEnabled(false);
        mHistoryRecycle=findViewById(R.id.iv_history_recycle);
        mHistoryRecycle.setLayoutManager(gridLayoutManager);
        mHistoryRecycle.setAdapter(mHistoryAdapter);

        mSearchText=findViewById(R.id.et_search);
        mClearText=findViewById(R.id.iv_clear);

        mPresenter.getSearch();
        setListener();
    }
    //???????????????
    private void setListener() {
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d("zhhr1122","beforeTextChanged="+s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("zhhr1122","onTextChanged="+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.d("zhhr1122","Editable="+s.toString());
                mResultRecycle.setVisibility(View.GONE);
                if(s.length()!=0){
                    //???????????????????????????????????????
                    mResultAdapter.setKey(s.toString());
                    mDynaicAdapter.setKey(s.toString());
                    mDynamicRecycle.setVisibility(View.VISIBLE);
                    mPresenter.getDynamicResult(s.toString());
                    mClearText.setVisibility(View.VISIBLE);
                }else{
                    mDynamicRecycle.setVisibility(View.GONE);
                    mClearText.setVisibility(View.GONE);
                }
            }
        });
        mTopAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mTopAdapter.getItems(position);
                IntentUtil.ToComicDetail(SearchActivity.this,comic.getId()+"",comic.getTitle());
            }
        });
        mDynaicAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mDynaicAdapter.getItems(position);
                IntentUtil.ToComicDetail(SearchActivity.this,comic.getId()+"",comic.getTitle());
            }
        });
        mResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mResultAdapter.getItems(position);
                IntentUtil.ToComicDetail(SearchActivity.this,comic.getId()+"",comic.getTitle(),comic.getFrom());
            }
        });
        mHistoryAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                mPresenter.getSearchResult(mHistoryAdapter.getItems(position).getTitle());
            }
        });
        //????????????????????????
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    //???????????????
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mPresenter.getSearchResult();
                    return true;
                }
                return false;
            }
        });

        mClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel(v);
            }
        });

        ivClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistory(v);
            }
        });
    }

    @Override
//    @OnClick(R.id.iv_clear)
    public void clearText() {
        mSearchText.setText("");
        mResultRecycle.setVisibility(View.GONE);
        mDynamicRecycle.setVisibility(View.GONE);
        mNormal.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
    }

    @Override
    public void fillDynamicResult(List<Comic> comics) {
        mDynamicRecycle.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        mDynaicAdapter.updateWithClear(comics);
    }

    @Override
    public void fillResult(List<Comic> comics) {
        mResultRecycle.setVisibility(View.VISIBLE);
        mDynamicRecycle.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mNormal.setVisibility(View.GONE);
        mResultAdapter.updateWithClear(comics);
        mResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void fillTopSearch(List<Comic> comics) {
        mTopAdapter.updateWithClear(comics);
        mTopAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSearchText(String title) {
        mSearchText.setText(title);
        mSearchText.setSelection(title.length());
        mClearText.setVisibility(View.VISIBLE);
    }

    @Override
    public String getSearchText() {
        return mSearchText.getText().toString();
    }


    @Override
    public void showErrorView(String title) {
        mError.setVisibility(View.VISIBLE);
        mError.setText(Html.fromHtml(TextUtils.getSearchErrorText(title),Html.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public void fillData(List<Comic> comics) {
        if(comics!=null&&comics.size()!=0){
            mHistoryAdapter.updateWithClear(comics);
        }else{
            mHistoryAdapter.onClear();
        }
        mHistoryAdapter.notifyDataSetChanged();
    }


    @Override
    public void getDataFinish() {
        mDynaicAdapter.notifyDataSetChanged();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

//    @OnClick(R.id.tv_cancel)
    public void Cancel(View view){
        finish();
    }

//    @OnClick(R.id.iv_clear_history)
    public void clearHistory(View view){
        mPresenter.clearHistory();
    }

}
