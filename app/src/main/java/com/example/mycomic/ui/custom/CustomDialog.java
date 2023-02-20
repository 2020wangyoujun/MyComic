package com.example.mycomic.ui.custom;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mycomic.R;

public class CustomDialog extends AlertDialog {
    private onClickListener listener;

    TextView mContent;
    TextView mTitle;
    String title;
    String content;
    TextView tvCancel,tvConfirm;
    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    public CustomDialog(Context context, String title, String content) {
        this(context, R.style.MyDialog);
        this.title = title;
        this.content = content;
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 预先设置Dialog的一些属性
       setContentView(R.layout.dialog_default);
        mTitle=findViewById(R.id.tv_title);
        mContent=findViewById(R.id.tv_content);
        mTitle.setText(title);
        mContent.setText(content);

        tvCancel=findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCancel();
            }
        });

        tvConfirm=findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickConfirm();
            }
        });
    }

    public void clickCancel(){
        if(listener!=null){
            listener.OnClickCancel();
        }
    }

    public void clickConfirm(){
        if(listener!=null){
            listener.OnClickConfirm();
        }
    }

    public interface onClickListener{
        void OnClickConfirm();
        void OnClickCancel();
    }
}



