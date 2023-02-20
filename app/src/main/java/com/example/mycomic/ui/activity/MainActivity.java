package com.example.mycomic.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mycomic.R;
import com.example.mycomic.ui.activity.base.BaseFragmentActivity;
import com.example.mycomic.ui.custom.FloatEditLayout;
import com.example.mycomic.ui.fragment.BookShelfFragment;
import com.example.mycomic.utils.ZToast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends BaseFragmentActivity {

    Button mHome;
    Button mBookShelf;
    Button mMine;


    FloatEditLayout mEditBottom;

    BookShelfFragment bookShelfFragment;


    private static final int SDCARD_REQUEST_CODE = 1;


    public FloatEditLayout getmEditBottom() {
        return mEditBottom;
    }

    @Override
    protected void initView() {
        requestSDCardPermission();
        mHome=findViewById(R.id.btn_home);

        mHome.setBackgroundResource(R.drawable.homepage_press);
        fragments = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        bookShelfFragment= (BookShelfFragment) fragmentManager.findFragmentById(R.id.fm_bookshelf);
        fragments.add (fragmentManager.findFragmentById(R.id.fm_home));
        fragments.add (bookShelfFragment);
        fragments.add (fragmentManager.findFragmentById(R.id.fm_mine));

        mEditBottom=findViewById(R.id.rl_edit_bottom);
        mEditBottom.setListener(new FloatEditLayout.onClickListener() {
            @Override
            public void OnClickSelect() {
                bookShelfFragment.OnClickSelect();
            }

            @Override
            public void OnDelete() {
                bookShelfFragment.OnClickDelete();
            }
        });
        selectTab(0);

        CheckVersion();

        mBookShelf=findViewById(R.id.btn_bookshelf);
        mMine=findViewById(R.id.btn_mine);
        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHomeFragment(v);
            }
        });
        mBookShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toBookShelfFragment(v);
            }
        });
        mMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMineFragment(v);
            }
        });

    }

    private String readStringFromInput(InputStream inputStream){
            StringBuilder builder = new StringBuilder();
            try {
                int tmp=0;
                while((tmp =inputStream.read())!=-1){
                    char c = (char) tmp;
                    if(c=='\r' && (char)inputStream.read()=='\n'){
                        break;
                    }
                    builder.append(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
    }


    /**
     * 检查新版本
     */
    private void CheckVersion() {
//        PgyUpdateManager.register(this, new UpdateManagerListener() {
//            @Override
//            public void onNoUpdateAvailable() {
//                LogUtil.d("没有发现新版本");
//            }
//
//            @Override
//            public void onUpdateAvailable(String result) {
//                final AppBean appBean = getAppBeanFromString(result);
//                LogUtil.d(appBean.toString());
//                final CustomDialog dialog = new CustomDialog(MainActivity.this,"自动更新","发现新版本:v"+appBean.getVersionName()+",是否更新?");
//                dialog.setListener(new CustomDialog.onClickListener() {
//                    @Override
//                    public void OnClickConfirm() {
//                        startDownloadTask(
//                                MainActivity.this,
//                                appBean.getDownloadURL());
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void OnClickCancel() {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//        });
    }


    private void requestSDCardPermission() {
        // 先判断有没有权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            writeFile();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SDCARD_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SDCARD_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                ZToast.makeText(this,"存储权限获取成功",3);
            } else {
                ZToast.makeText(this,"存储权限获取失败",3);
            }
        }
    }

    public void quitEdit(){
//        setEditBottomVisible(View.GONE);
//        bookShelfFragment.quitEdit();
    }

    public void toHomeFragment(View view){
        selectTab(0);
        resetBottomBtn();
        mHome.setBackgroundResource(R.drawable.homepage_press);
        initStatusBar(true);
    }

    public void toBookShelfFragment(View view){
        selectTab(1);
        resetBottomBtn();
        mBookShelf.setBackgroundResource(R.drawable.bookshelf_press);
        initStatusBar(false);
    }

    public void toMineFragment(View view){
        selectTab(2);
        resetBottomBtn();
        mMine.setBackgroundResource(R.drawable.mine_press);
        initStatusBar(false);
    }

    public void resetBottomBtn(){
        mHome.setBackgroundResource(R.drawable.homepage);
        mMine.setBackgroundResource(R.drawable.mine);
        mBookShelf.setBackgroundResource(R.drawable.bookshelf);
    }

    public void setEditBottomVisible(int Visible){
        mEditBottom.setVisibility(Visible);
    }

}