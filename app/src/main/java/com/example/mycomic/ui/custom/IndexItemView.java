package com.example.mycomic.ui.custom;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycomic.R;
import com.example.mycomic.utils.DisplayUtil;

public class IndexItemView extends RelativeLayout {
    private TextView mTitle;

    private View view;
    private Drawable img_location;
    private onItemClickLinstener listener;

    public onItemClickLinstener getListener() {
        return listener;
    }

    public void setListener(onItemClickLinstener listener) {
        this.listener = listener;
    }

    public TextView getmTitle() {
        return mTitle;
    }

    public IndexItemView(Context context, String title, final int position) {
        super(context);
        img_location = context.getResources().getDrawable(R.mipmap.location,context.getTheme());
        img_location.setBounds(0, 0, img_location.getMinimumWidth(), img_location.getMinimumHeight());
        view = LayoutInflater.from(getContext()).inflate(R.layout.item_detail, null);
        addView(view);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mTitle.setText((position+1)+"-"+title);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onItemClick(view,position);
                }
            }
        });
    }

    public interface onItemClickLinstener{
        void onItemClick(View view, int position);
    }

    public void setCurrentColor(boolean isCurrent){
        if(isCurrent){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                mTitle.setTextAppearance(R.style.colorTextPrimary);
//            }else{
//                mTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
//            }
            mTitle.setTextAppearance(R.style.colorTextPrimary);
            mTitle.setCompoundDrawables(null, null, img_location, null);
            mTitle.setCompoundDrawablePadding(DisplayUtil.dip2px(getContext(),10));
        }else{
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                mTitle.setTextAppearance(R.style.colorTextBlack);
//            }else{
//                mTitle.setTextColor(ContextCompat.getColor(getContext(),R.color.colorTextBlack));
//            }
            mTitle.setTextAppearance(R.style.colorTextBlack);
            mTitle.setCompoundDrawables(null, null, null, null);
        }
    }

}
