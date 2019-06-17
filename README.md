# EatPaper
This is the repo for final project of Android App Development Course @NCKU CSIE, Spring, 2019.

## Intro
這是一個紙品庫存管理的 App，預設的使用者是影印店的員工，能夠將店裡的紙品新增到 Google 的 Cloud FireStore 資料庫，並且能夠隨時檢視紙品清單，並有出庫、入庫的功能，也能檢視數量變動的歷史紀錄。
## Prototype
[Link to prototype](https://github.com/jayhung97724/EatPaper/tree/master/EatPaper%20Prototype)

## App Overview

0. 登入/註冊

    ![image alt](https://github.com/jayhung97724/EatPaper/blob/master/Screenshots/Screenshot_1.png?raw=true)

    ![image alt](https://github.com/jayhung97724/EatPaper/blob/master/Screenshots/Screenshot_2.png?raw=true)

1. Launch 主畫面

    ![](https://github.com/jayhung97724/EatPaper/blob/master/Screenshots/Screenshot_3.png?raw=true)

2. 新增紙品

    ![](https://github.com/jayhung97724/EatPaper/blob/master/Screenshots/Screenshot_4.png?raw=true)
    
    

3. 檢視紙庫

    ![](https://github.com/jayhung97724/EatPaper/blob/master/Screenshots/paperlist_1.png?raw=true)
    
    ![](https://github.com/jayhung97724/EatPaper/blob/master/Screenshots/paperlist_2.png?raw=true)
    
    ![](https://github.com/jayhung97724/EatPaper/blob/master/Screenshots/paperlist_3.png?raw=true)

4. 紙張詳情/歷史紀錄

    ![](https://github.com/jayhung97724/EatPaper/blob/master/Screenshots/Screenshot_6.png?raw=true)

5. 出入庫
    ![](https://github.com/jayhung97724/EatPaper/blob/master/Screenshots/Screenshot_7.png?raw=true)

## Specs
<!-- ![](https://i.imgur.com/3au2Ew3.png) -->

### 0. 登入/註冊
* **使用Email進行登入的畫面 **
  1.目前Firebase 的library 支援的最小SdkVersion為16
    將minSdkVersion 設定16
  ![](https://imgur.com/oYmeVGI.png)
  
  2. 需要再build.gradle(Module)引入的套件包含

### 1. Launch 主畫面
@至柔

### 2. 新增紙品
* **使用ColorPicker選擇顏色**

    1.需要在build.gradle(Module：app)引入套件

    `implementation 'com.github.yukuku:ambilwarna:2.0.1'`
        
     ￼![](https://i.imgur.com/cbCQIg2.jpg)
     
    2.ColorPicker功能設定
    
    ```java=
    //呼叫對話方塊
    AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, colorID, new AmbilWarnaDialog.OnAmbilWarnaListener() {
         @Override
         public void onCancel(AmbilWarnaDialog dialog) {
            //取消操作
         }
         @Override
         public void onOk(AmbilWarnaDialog dialog, int color) {
             //確認操作
        }
    });
    colorPicker.show();
    ```

    ![](https://i.imgur.com/2klg1Nk.png)


### 3. 檢視紙庫
檢視紙庫主要包含兩個部分: `PaperListAdapter`、`PaperListActivity`

1. `PaperListAdapter`
    需繼承 FirestoreAdapter 以使用 FireStore 的函式來取得 FireBase 的資料
    主要功能是將 FireBase 取得的資料顯示在 RecyclerView 中以顯示
    引用 `Glide` 函式庫以快速存取紙種圖片

2. `PaperListActivity`
    主要功能是依照 spinner 中的篩選和排序條件來顯示 FireBase 中的資料並讓使用者選擇紙品跳到該紙品的詳細資訊
    - 顯示資料
        利用 setOnItemSelectedListener 監聽 spinner 並取得篩選和排序條件
        利用 FireBase 的 whereEqualTo 和 orderBy 來實現篩選和排序
        透過 `PaperListAdapter` 將資料顯示在 RecyclerView 中
    
    - 顯示紙品詳細資訊
        implement `PaperListAdapter` 中的 OnPaperSelectedListener 來監聽紙品的選擇事件
        當使用者選擇了某紙品後，呼叫 `PaperDetailActivity` 利用 PaperID 顯示對應的紙品細節
    ```java=
    public void onPaperSelected(DocumentSnapshot paper) {
        Intent intent = new Intent(this, PaperDetailActivity.class);
        intent.putExtra(PaperDetailActivity.KEY_PAPER_ID, paper.getId());
        startActivity(intent);
    }
    ```
    

### 4. 紙張詳情/歷史紀錄

1. `PaperDetailActivity` : **紙品細節**
    由 `PaperListActivity` 所列出的紙品來呼叫，並在 ```new Intent``` 時，會將該紙品對應到的 FireStore document ID 放在 Intent Extra 裡，並在本 Activity 去 Query 該紙品 document 並將細節顯示出來
    ```java=
    // Get paper ID from extras
    String paperId = getIntent().getExtras().getString(KEY_PAPER_ID);
    
    // Initialize Firestore
    mFirestore = FirebaseFirestore.getInstance();

    // Get reference to the restaurant
    mPaperRef = mFirestore.collection("paperStock").document(paperId);
    ```

2. `HistoryAdapter` : **歷史紀錄**
        負責將該紙品的數量紀錄在 `ListView` 中顯示出來。
        繼承 `BaseAdapter` 並實作 `getView()` 顯示自定義的 layout。
        
### 5. 出入庫
- `PaperActionDialog` : **出/入庫動作訊息框**
        繼承 `DialogFragment` 並實作 `onCreateView()`、`onAttach()`、`onResume()`，在點按 Action Button 之後跳出該對話框，讓使用者輸入該動作的出/入庫的變動量，完成之後對 FireStore 進行 `update`。

## Cloud FireStore Database Structure
```
paperStock {collection}
└─ paper {document}
    ├─ currentQuantity: number
    ├─ paperColor: String 
    ├─ paperID: String
    ├─ paperName: String
    ├─ paperSize: String
    ├─ paperType: String
    ├─ paperWeight: number
    ├─ supplyCompany: String 
    ├─ unit: String
    └─ history: Array
        ├─ index: number
        ├─ action: String
        ├─ quantity: number
        └─ updateTime: TimeStamp
``` 

## Contributions

| Student ID | Name    | Contribution |
| --------  | -------- | -------- |
| P76071129 | 高至柔    |[登入/註冊](#0-登入註冊)、[Launch 主畫面](#1-Launch-主畫面)|
| P76071399 | 陳彥儒    |[檢視紙庫](#2-檢視紙庫)|
| Q56074108 | 盧晏慈    |[新增紙品](#3-新增紙品)|
| Q56084022 | 洪浩祐    |[紙張詳情/歷史紀錄](#4-紙張詳情歷史紀錄)、[出入庫](#5-出入庫)|

## APK Downloads

| Release | Version    | Info |
| --------  | -------- | -------- |
|[**EatPaper.apk**](https://github.com/jayhung97724/EatPaper/blob/master/release/EatPaper.apk?raw=true)|0.0|Beta Test|
|[**EatPaper-v1.0.apk**](https://github.com/jayhung97724/EatPaper/blob/master/release/EatPaper-v1.0.apk?raw=true)|1.0|Add filter function, change quantity sorting to `ASCENDING`|
