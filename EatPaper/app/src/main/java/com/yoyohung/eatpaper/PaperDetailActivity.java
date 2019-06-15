package com.yoyohung.eatpaper;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.yoyohung.eatpaper.adapter.HistoryAdapter;
import com.yoyohung.eatpaper.model.Paper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class PaperDetailActivity extends AppCompatActivity
    // implement listener
        implements EventListener<DocumentSnapshot>    {
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
    private View mLower;
    private Button mInStock;
    private Button mOutStock;
    private ListView mListView;
    // ---------------------

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
        mLower = (View) findViewById(R.id.view_color_lower);
        mInStock = (Button) findViewById(R.id.button_inStock);
        mOutStock = (Button) findViewById(R.id.button_outStock);
        mListView = (ListView) findViewById(R.id.listView_history);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.paper_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Listener for the Paper document ({@link #mPaperRef}).
     */
    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "paper:onEvent", e);
            return;
        }
//        Paper paper = snapshot.toObject(Paper.class);
        Log.d(TAG, "DocumentSnapshot data: " + snapshot.getData());
        onPaperLoaded(snapshot.toObject(Paper.class));
    }

    private void onPaperLoaded(Paper paper) {
        String IDLabel = paper.getSupplyCompany() + " " + paper.getPaperID();
        mIDLabel.setText(IDLabel);
        mPaperName.setText(paper.getPaperName());
        mSize.setText(paper.getPaperSize());
        mWeight.setText(String.valueOf(paper.getPaperWeight()));
        mCurrentQuantity.setText(String.valueOf(paper.getCurrentQuantity()));
        mUnit.setText(paper.getUnit());
        mUpper.setBackgroundColor(Color.parseColor(paper.getPaperColor()));
        mLower.setBackgroundColor(Color.parseColor(paper.getPaperColor()));

//        Collections.sort(paper.getHistory(), new Comparator<Map<String, Object>>() {
//            @Override
//            public int compare(Map<String, Object> h1, Map<String, Object> h2) {
//                return h1.get("quantity").compareTo(h2.get("quantity"));
//            }
//        });
        List history = paper.getHistory();
        Collections.sort(history, new Comparator<Map<String, Object>> () {
            public int compare(Map<String, Object> m1, Map<String, Object> m2) {
                return ((Long) m2.get("index")).compareTo((Long) m1.get("index")); //ascending order
                //return ((Integer) m2.get("num")).compareTo((Integer) m1.get("num")); //descending order
            }
        });
        HistoryAdapter adapter = new HistoryAdapter(this, history);
        mListView.setAdapter(adapter);
//        Map<String, Object> update = (Map<String, Object>) paper.getHistory().get(0);
//        Timestamp stamp = (Timestamp) update.get("updateTime");
//        Date date = stamp.toDate();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String dts = sdf.format(date);
//        Log.d(TAG, dts);
//        Log.d(TAG, (String) update.get("action"));
//        Log.d(TAG, String.valueOf(update.get("quantity")));
//        mInStock.setText();
//        mOutStock.setText();

    }
}
