package com.yoyohung.eatpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.yoyohung.eatpaper.adapter.PaperListAdapter;
import com.yoyohung.eatpaper.model.Paper;

import java.util.ArrayList;

public class PaperListActivity extends AppCompatActivity implements
        PaperListAdapter.OnPaperSelectedListener,
        View.OnClickListener{
    // member variables
    private static final String TAG = "PaperListActivity";
    private RecyclerView mRecyclerView;
    private ArrayList<Paper> mPapersData;
    private PaperListAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    //private static final int LIMIT = 20;

    // spinner
    private Spinner sorter_spinner;
    private String[] sorter_list;
    private String sorter_type;

    private Spinner filter_spinner;
    private String[] filter_list;
    private String filter_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_list);

        // init spinner variable
        sorter_spinner = findViewById(R.id.spinner_sorter);
        sorter_list = getResources().getStringArray(R.array.sorter_list);
        sorter_type = sorter_list[0];
        filter_spinner = findViewById(R.id.spinner_filter);
        filter_list = getResources().getStringArray(R.array.filter_list);
        filter_type = filter_list[0];

        // init filter spinner
        sorter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sorter_type = sorter_list[position];
                //onSorter(sorter_type);
                onSorterFilter(sorter_type, filter_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sorter_type = sorter_list[0]; // default : stock
            }
        });

        // init filter spinner
        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_type = filter_list[position];
                onSorterFilter(sorter_type, filter_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filter_type = filter_list[0];
            }
        });

        // init firestore and main recyclerView
        initFirestore();

        // init the recyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        initRecyclerView();

    }

    private void initFirestore(){
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // get firestore
        mFirestore = FirebaseFirestore.getInstance();

        // get paper list
        mQuery = mFirestore.collection("paperStock")
                .orderBy("paperName", Query.Direction.DESCENDING);
        //.limit(LIMIT);
    }

    private void initRecyclerView(){
        if(mQuery == null){
            Log.w(TAG, "No query, not initializing RecyclerView");
        }
        // set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new PaperListAdapter(mQuery, this){
            @Override
            protected void onDataChanged(){

            }

            @Override
            protected void onError(FirebaseFirestoreException e){

            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public  void onStart(){
        super.onStart();

        // start listening for firestore updates
        if(mAdapter != null){
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAdapter != null){
            mAdapter.stopListening();
        }
    }

    @Override
    public void onPaperSelected(DocumentSnapshot paper) {
        Intent intent = new Intent(this, PaperDetailActivity.class);
        intent.putExtra(PaperDetailActivity.KEY_PAPER_ID, paper.getId());
        startActivity(intent);
    }

    public void onSorter(String sorter_type){
        // construct basic query
        Query query = mFirestore.collection("paperStock");

        switch(sorter_type){
            case "紙名" :
                query = query.orderBy("paperName", Query.Direction.DESCENDING);
                break;
            case "庫存量":
                query = query.orderBy("currentQuantity", Query.Direction.ASCENDING);
                break;
            case "顏色":
                query = query.orderBy("paperColor", Query.Direction.DESCENDING);
                break;
            case "紙品種類":
                query = query.orderBy("paperType", Query.Direction.DESCENDING);
                break;
            default:
                query = query.orderBy("paperName", Query.Direction.DESCENDING);
                break;
        }

        // update the query
        mQuery = query;
        mAdapter.setQuery(query);
        //mAdapter.notifyDataSetChanged();
    }

    public void onSorterFilter(String sorter_type, String filter_type){
        // construct basic query
        Query query = mFirestore.collection("paperStock");

        // filter
        switch (filter_type){
            case "所有紙種":
                break;
            case "封面紙":
                query = query.whereEqualTo("paperType", "封面紙");
                break;
            case "色紙":
                query = query.whereEqualTo("paperType", "色紙");
                break;
            case "影印紙":
                query = query.whereEqualTo("paperType", "影印紙");
                break;
            case "貼紙":
                query = query.whereEqualTo("paperType", "貼紙");
                break;
            case "上光膜":
                query = query.whereEqualTo("paperType", "上光膜");
                break;
            case "大圖捲紙":
                query = query.whereEqualTo("paperType", "大圖捲紙");
                break;
            default:
                break;
        }

        // sorter
        switch(sorter_type){
            case "紙名" :
                query = query.orderBy("paperName", Query.Direction.DESCENDING);
                break;
            case "庫存量":
                query = query.orderBy("currentQuantity", Query.Direction.ASCENDING);
                break;
            case "顏色":
                query = query.orderBy("paperColor", Query.Direction.DESCENDING);
                break;
            case "紙品種類":
                query = query.orderBy("paperType", Query.Direction.DESCENDING);
                break;
            default:
                query = query.orderBy("paperName", Query.Direction.DESCENDING);
                break;
        }

        // update the query
        mQuery = query;
        mAdapter.setQuery(query);
    }

    @Override
    public void onClick(View view) {

    }
}
