package com.yoyohung.eatpaper;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yoyohung.eatpaper.model.Paper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddPaper extends AppCompatActivity {
    int colorID;
    String StrPaperType;       //種類內容
    String StrPaperlabel;       //廠商內容
    String StrPaperunit;       //廠商內容
    ImageView PaperColor;    //顏色顯示
    Spinner PaperType;        //分類選項
    Spinner PaperLabel;       //廠商選項
    Spinner PaperUnit;        //紙張單位輸入項
    EditText PaperID;         //紙品編號輸入項
    EditText PaperName;       //紙品名稱輸入項
    EditText PaperWeight;     //紙品基重輸入項
    EditText PaperQuantity;    //紙品數量輸入項
    TextInputEditText PaperSize;       //紙品尺寸輸入項
    TextView TVPaperType;
    TextView TVPaperLabel;
    TextView TVPaperName;
    TextView TVPaperQuantity;
    TextView TVRemark;
    ColorStateList oldColors;
    //--
    private FirebaseFirestore mFirestore;
    //
    private static final String Action="action";
    private static final String Index="index";
    private static final String Quantity="quantity";
    private static final String UpdateTime="updateTime";
    String[] ItemPaperType;
    String[] ItemPaperLabel;
    String[] ItemPaPerUnit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paper);
        // 返回Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();
        //設定輸入項
        PaperColor = findViewById(R.id.Ima_PaperColor);
        PaperType  =(Spinner) findViewById(R.id.Sp_PaperType);
        PaperLabel =(Spinner) findViewById(R.id.Sp_PaperLabel);
        PaperUnit  =(Spinner) findViewById(R.id.Sp_PaperUnit);
        PaperID  =(EditText) findViewById(R.id.ET_ID);
        PaperName =(EditText) findViewById(R.id.ET_PaperName);
        PaperWeight =(EditText) findViewById(R.id.ET_PaperWeight);
        PaperQuantity  =(EditText) findViewById(R.id.ET_PaperQuantity);
        PaperSize = findViewById(R.id.ET_PaperSize);
        //設定標籤
        TVPaperType = findViewById(R.id.TV_Type);
        TVPaperLabel = findViewById(R.id.TV_Label);
        TVPaperName = findViewById(R.id.TV_Name);
        TVPaperQuantity = findViewById(R.id.TV_Quantity);
        TVRemark = findViewById(R.id.TV_Remark);
        oldColors =  TVPaperQuantity.getTextColors();
        //
        ItemPaperType = getResources().getStringArray(R.array.PaperType);
        ItemPaperLabel = getResources().getStringArray(R.array.PaperLabel);
        ItemPaPerUnit  = getResources().getStringArray(R.array.PaperUnit);
        //取得色碼
        ColorDrawable D_Color = (ColorDrawable) PaperColor.getBackground();  //取得現在色碼
        colorID = D_Color.getColor();  //轉成色碼
        PaperColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();   //開啟顏色選擇視窗
            }
        });
        //取得種類
        PaperType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrPaperType = ItemPaperType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //取得廠商
        PaperLabel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrPaperlabel = ItemPaperLabel[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ///取得單位
        PaperUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StrPaperunit = ItemPaPerUnit[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });    //選擇顏色

    }
    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, colorID, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                colorID= color;
                PaperColor.setBackgroundColor(color);
            }
        });
        colorPicker.show();
    }


    public void OnSave(View view) {
        String StrPaperName= PaperName.getText().toString();
        if ((! "".equals(StrPaperName.trim())) &&  (! "".equals(PaperQuantity.getText().toString().trim()))){
            CollectionReference Papers = mFirestore.collection("paperStock");
            String hex_color="#"+Integer.toHexString(colorID);
            Date CurrentTime = new Date(System.currentTimeMillis()) ;
            Timestamp updatetime=new Timestamp(CurrentTime);
            List<Map<String,Object>> newhistory = new ArrayList<Map<String,Object>>();
            Map<String,Object> npaper =new HashMap<String,Object>();
            npaper.put(Action,"in");
            npaper.put(Index,0);
            npaper.put(Quantity,Integer.parseInt(PaperQuantity.getText().toString()));
            npaper.put(UpdateTime,updatetime);
            newhistory.add(npaper);
            Paper newpaper = new Paper(PaperID.getText().toString(),StrPaperType,StrPaperName,
                    PaperSize.getText().toString(),hex_color,StrPaperunit,StrPaperlabel,Integer.parseInt(PaperWeight.getText().toString()),
                    Integer.parseInt(PaperQuantity.getText().toString()),newhistory);

            Papers.add(newpaper);
            Toast.makeText(this, "成功新增紙品\n"+StrPaperName, Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{   //提醒必填項目
            TVRemark.setText("必填");
            ColorStateList R_Color = TVRemark.getTextColors();
            if ("".equals(StrPaperName.trim())){ TVPaperName.setTextColor(R_Color);}
            else {TVPaperName.setTextColor(oldColors);}
            if (("".equals(PaperQuantity.getText().toString().trim()))||(PaperQuantity.getText().toString() == "0")){ TVPaperQuantity.setTextColor(R_Color);}
            else { TVPaperQuantity.setTextColor(oldColors);}
        }
    }
}
