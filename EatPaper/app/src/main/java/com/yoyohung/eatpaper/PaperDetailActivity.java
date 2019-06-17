package com.yoyohung.eatpaper;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
//import android.util.Log;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.yoyohung.eatpaper.adapter.HistoryAdapter;
import com.yoyohung.eatpaper.model.Paper;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;


public class PaperDetailActivity extends AppCompatActivity
        // implement listener
        implements EventListener<DocumentSnapshot>, PaperActionDialog.PaperActionDialogListener {
    private static final String TAG = "PaperDetailActivity";
    public static final String KEY_PAPER_ID = "key_paper_id";
    private FirebaseFirestore mFirestore;
    private DocumentReference mPaperRef;
    private ListenerRegistration mPaperRegistration;

    private TextView mIDLabel;
    private TextView mPaperName;
    private TextView mSize;
    private TextView mWeight;
    private TextView mCurrentQuantity;
    private TextView mUnit;
    private View mUpper;
    private Button mInStock;
    private Button mOutStock;
    private ListView mListView;
    // ---------------------
    private PaperActionDialog mActionDialog;
    // TextView mDFCurrentQuantity;
    // TextView mDFAction;
    // EditText mDFDelta;
    // TextView mDFNewQuantity;
    private int currentQuantity;
    private int index;
    private String unit;
    private String action;
    private String paperName;
    // --------------------*-

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_paper_detail);

        // Get paper ID from extras
        String paperId = getIntent().getExtras().getString(KEY_PAPER_ID);
        if (paperId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_PAPER_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get reference to the restaurant
        mPaperRef = mFirestore.collection("paperStock").document(paperId);

        mIDLabel = (TextView) findViewById(R.id.textView_idLabel_paper_detail);
        mPaperName = (TextView) findViewById(R.id.textView_nameLabel_paper_detail);
        mSize = (TextView) findViewById(R.id.textView_size_paper_detail);
        mWeight = (TextView) findViewById(R.id.textView_weight_paper_detail);
        mCurrentQuantity = (TextView) findViewById(R.id.textView_currentQuantity_paper_detail);
        mUnit = (TextView) findViewById(R.id.textView_unit_paper_detail);
        mUpper = (View) findViewById(R.id.view_color_upper);
        mInStock = (Button) findViewById(R.id.button_inStock);
        mOutStock = (Button) findViewById(R.id.button_outStock);
        mListView = (ListView) findViewById(R.id.listView_history);
        // ******************************
        // mDFCurrentQuantity = findViewById(R.id.textView_current_quantity);
        // mDFAction = findViewById(R.id.textView_action_label);
        // mDFDelta = findViewById(R.id.editText_delta);
        // mDFNewQuantity = findViewById(R.id.textView_new_quantity);

        mInStock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                action = "in";
                onActionClicked(view, action);
            }
        });

        mOutStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = "out";
                onActionClicked(view, action);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mPaperRegistration = mPaperRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPaperRegistration != null) {
            mPaperRegistration.remove();
            mPaperRegistration = null;
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.paper_detail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * Listener for the Paper document ({@link #mPaperRef}).
     */
    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            // Log.w(TAG, "paper:onEvent", e);
            return;
        }
        // Paper paper = snapshot.toObject(Paper.class);
        // Log.d(TAG, "DocumentSnapshot data: " + snapshot.getData());
        onPaperLoaded(snapshot.toObject(Paper.class));
    }

    private void onPaperLoaded(Paper paper) {
        String IDLabel = paper.getSupplyCompany() + " " + paper.getPaperID();
        mIDLabel.setText(IDLabel);
        // Log.d(TAG, "paper.getPaperName(): " + String.valueOf(paper.getNewQuantity()));
        // *****************
        paperName = paper.getPaperName();
        currentQuantity = paper.getCurrentQuantity();
        unit = paper.getUnit();
        // *****************
        mPaperName.setText(paper.getPaperName());
        mSize.setText(paper.getPaperSize());
        mWeight.setText(String.valueOf(paper.getPaperWeight()));
        mCurrentQuantity.setText(String.valueOf(currentQuantity));
        mUnit.setText(unit);
        mUpper.setBackgroundColor(Color.parseColor(paper.getPaperColor()));

        List history = paper.getHistory();
        Collections.sort(history, new Comparator<Map<String, Object>> () {
            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                return ((Long) m2.get("index")).compareTo((Long) m1.get("index")); //ascending order
                //return ((Integer) m2.get("num")).compareTo((Integer) m1.get("num")); //descending order
            }
        });
        index = history.size();
        HistoryAdapter adapter = new HistoryAdapter(this, history);
        mListView.setAdapter(adapter);

    }

    public void onActionClicked(View view, String action) {
        mActionDialog = PaperActionDialog.newInstance(currentQuantity, action, unit);
        mActionDialog.show(getSupportFragmentManager(), PaperActionDialog.TAG);
    }

    @Override
    public void setNewQuantity(final int newQuantity) {
        hideKeyboard();
        if (newQuantity >= 0) {
            Log.d(TAG, "Get: " + String.valueOf(newQuantity));
            Date CurrentTime = new Date(System.currentTimeMillis());
            Timestamp updateTime = new Timestamp(CurrentTime);
            Map<String,Object> newAction = new HashMap<String,Object>();
            newAction.put("action", action);
            newAction.put("index", index);
            newAction.put("quantity", newQuantity);
            newAction.put("updateTime", updateTime);
            newAction.put("delta", abs(newQuantity - currentQuantity));

            mPaperRef.update("currentQuantity", newQuantity);
            mPaperRef.update("history", FieldValue.arrayUnion(newAction));
            Snackbar.make(findViewById(R.id.view_paper_list_color), "成功修改 " + paperName + " 數量", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(findViewById(R.id.view_paper_list_color), "數量不正確，請再確認一次", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
