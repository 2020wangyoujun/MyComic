package com.example.mycomic.ui.view;

public interface IChapterView<T> extends ILoadDataView<T>{
    //弹出菜单
    void showMenu();
    //下一章
    void nextChapter(T data, int loadingPosition, int offset);
    //前一章
    void preChapter(T data, int loadingPosition, int offset);
    //切换预览模式
    void SwitchModel(int a);
    //前一页
    void prePage();
    //下一页
    void nextPage();

    void setTitle(String name);

    void SwitchSkin();

    void setSwitchNightVisible(int visible,boolean isNight);
}
