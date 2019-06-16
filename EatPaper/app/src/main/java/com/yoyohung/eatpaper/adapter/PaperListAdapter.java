package com.yoyohung.eatpaper.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.yoyohung.eatpaper.R;
import com.yoyohung.eatpaper.model.Paper;

public class PaperListAdapter extends FirestoreAdapter<PaperListAdapter.ViewHolder> {
    private static final String TAG = "PaperListAdapter";

    public interface OnPaperSelectedListener {
        void onPaperSelected(DocumentSnapshot paper);
    }

    private OnPaperSelectedListener mListener;

    public PaperListAdapter(Query query, OnPaperSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    /**
     * Constructor that passes in the sports data and the context.
     *
     * @param papersData ArrayList containing the papers data.
     * @param context Context of the application.
     */
    /*PaperListAdapter(Context context, ArrayList<Paper> papersData) {
        this.mPaperDatas = papersData;
        this.mContext = context;
    }*/

    /**
     * Required method for creating the viewholder objects.
     *
     * @param parent   The ViewGroup into which the new View will be added
     *                 after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly created ViewHolder.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_paper, parent, false));
    }

    /**
     * Required method that binds the data to the viewholder.
     *
     * @param holder   The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*// get current paper
        Paper currPaper = mPaperDatas.get(position);

        // populate the variable with data
        holder.bindTo(currPaper);*/
        holder.bind(getSnapshot(position), mListener);
    }

    /**
     * Required method for determining the size of the data set.
     *
     * @return Size of the data set.
     */
    /*@Override
    public int getItemCount() {
        return mPaperDatas.size();
    }*/

    /**
     * ViewHolder class that represents each row of data in the RecyclerView.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView paperTypeImageImage;
        TextView paperName;
        TextView paperIntroduction;
        TextView numberOfPaper;
        View paperColor;

        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder().
         *
         * @param itemView The rootview of the item_paper.xmll layout file.
         */
        public ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            paperTypeImageImage = itemView.findViewById(R.id.paper_type_image);
            paperName = itemView.findViewById(R.id.paper_name);
            paperIntroduction = itemView.findViewById(R.id.paper_introduction);
            numberOfPaper = itemView.findViewById(R.id.stock_of_paper);
            paperColor = itemView.findViewById(R.id.view_paper_list_color);

            // Set the OnClickListener to the entire view.
            //itemView.setOnClickListener(this);
        }

        /*void bindTo(Paper currPaper) {
            // populate the data
            paperName.setText(currPaper.getName());
            paperIntroduction.setText(currPaper.getIntroduction());
            numberOfPaper.setText(String.valueOf(currPaper.getStock()));
            // set image
            Glide.with(mContext).load(currPaper.getImageResource()).into(paperTypeImageImage);

        }*/

        public void bind(final DocumentSnapshot snapshot, final OnPaperSelectedListener listener) {
            Paper paper = snapshot.toObject(Paper.class);
            Log.d(TAG, "snapshot: "+snapshot.getData());
            Resources resources = itemView.getResources();

            switch (paper.getPaperType()){
                case "封面紙":
                    Glide.with(paperTypeImageImage.getContext())
                            .load(R.drawable.cover)
                            .into(paperTypeImageImage);
                    break;
                case "色紙":
                    Glide.with(paperTypeImageImage.getContext())
                            .load(R.drawable.color)
                            .into(paperTypeImageImage);
                    break;
                case "影印紙":
                    Glide.with(paperTypeImageImage.getContext())
                            .load(R.drawable.copy)
                            .into(paperTypeImageImage);
                    break;
                case "貼紙":
                    Glide.with(paperTypeImageImage.getContext())
                            .load(R.drawable.sticker)
                            .into(paperTypeImageImage);
                    break;
                case "上光膜":
                    Glide.with(paperTypeImageImage.getContext())
                            .load(R.drawable.varnish)
                            .into(paperTypeImageImage);
                    break;
                case "大圖捲紙":
                    Glide.with(paperTypeImageImage.getContext())
                            .load(R.drawable.roll)
                            .into(paperTypeImageImage);
                    break;
            }

            paperName.setText(paper.getPaperName());
            // String intro = paper.getPaperID() + " , " + paper.getPaperSize() + " , " + paper.getPaperWeight();
            String intro = paper.getPaperSize() + " , " + paper.getPaperWeight();
            paperIntroduction.setText(intro);
            numberOfPaper.setText(String.valueOf(paper.getCurrentQuantity()));
            paperColor.setBackgroundColor(Color.parseColor(paper.getPaperColor()));
            // click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onPaperSelected(snapshot);
                    }
                }
            });
        }
    }
}
