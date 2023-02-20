## 应用简介
  一款小而精美的漫画app。
  架构：MVP
  主要开源框架：retrofit2,rxjava3,room,glide,rxlifecycle4
  语言：java
## 功能简介
- 主页
   -  排行榜
   -  分类
   -  最新
   -  搜索（模糊搜索）
- 书架
   - 收藏列表
   - 历史记录
   - 下载列表
- 我的
   - 清除缓存
   - 问题反馈
   - 关于
- 漫画阅读
   - 漫画详情
   - 漫画阅读（预加载每次会提前加载前后两章，阅读时无缝切换到下一章）
   - 模式切换（正常模式（左往右划） 日漫模式（右往左化），卷轴模式（上下划））
   - 漫画下载（数据库使用room进行存储）
## 应用截图
### 首页:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/home1.jpg?raw=true)

![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/home2.jpg?raw=true)
### 排行:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/rank1.jpg?raw=true)
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/rank2.jpg?raw=true)
### 分类:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/category1.jpg?raw=true)
### 新作:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/new1.jpg?raw=true)
### 搜索:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/search1.jpg?raw=true)
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/search2.jpg?raw=true)
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/search3.jpg?raw=true)
### 漫画详情:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/detail1.jpg?raw=true)
### 阅读:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/view1.jpg?raw=true)
##### 缩放后:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/view2.jpg?raw=true)
##### 阅读模式切换:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/view3.jpg?raw=true)
### 下载:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/download1.jpg?raw=true)
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/download2.jpg?raw=true)
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/download3.jpg?raw=true)
### 我的:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/mine1.jpg?raw=true)
### gif演示:
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/home1.gif?raw=true)
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/home2.gif?raw=true)
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/bookshelf.gif?raw=true)
![image](https://github.com/2020wangyoujun/MyComic/blob/master/images/mine.gif?raw=true)
## API说明
大量资源来自腾讯动漫网站，通过jsoup解析html获取，传输效率较低，因此首页部分加载会比较慢。但漫画阅读部分用的时腾讯动漫app的api，这样会快很多。
本项目仅供学习，不提供成品app，严禁用于商业用途。