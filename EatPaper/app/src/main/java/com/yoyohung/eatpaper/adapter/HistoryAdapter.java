package com.yoyohung.eatpaper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.yoyohung.eatpaper.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class HistoryAdapter extends BaseAdapter {
    private Context context; //context
    private List<Map<String, Object>> history; //data source of the list adapter

    public HistoryAdapter(Context context, List<Map<String, Object>> history) {
        this.context = context;
        this.history = history;
    }
    @Override
    public int getCount() {
        return history.size();
    }

    @Override
    public Object getItem(int position) {
        return history.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.history_list_view, viewGroup, false);
        }

        TextView textView_timestamp = view.findViewById(R.id.textView_timestamp_paper_detail);
        TextView textView_quantity = view.findViewById(R.id.textView_quantity_paper_detail);
        TextView textView_action = view.findViewById(R.id.textView_action_paper_detail);

        Map<String, Object> update = (Map<String, Object>) getItem(position);
        Timestamp stamp = (Timestamp) update.get("updateTime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dts = sdf.format(stamp.toDate());
        Log.d("PaperDetailActivity", dts);

        textView_timestamp.setText(dts);
        textView_quantity.setText(update.get("delta").toString());
        String action = (String) update.get("action");
        if (action.equals("in")) {
            textView_action.setText("+");
        } else {
            textView_action.setText("–");
        }

        return view;
    }
}