package com.yoyohung.eatpaper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PaperActionDialog extends DialogFragment {

    public static final String TAG = "PaperActionDialog";

    TextView mHeader;
    TextView mCurrentQuantity;
    TextView mAction;
    EditText mDelta;
    TextView mNewQuantity;
    TextView mUnit;
    Button mOK;
    Button mCancel;
    private static int currentQuantity;
    private static String action;
    private int newQuantity;
    private static String unit;
    private PaperActionDialogListener listener;

    public interface PaperActionDialogListener {
        void setNewQuantity(int newQuantity);
    }


    public static PaperActionDialog newInstance(int cpQuantity, String cpAction, String cpUnit) {
        PaperActionDialog actionDialog = new PaperActionDialog();
        currentQuantity = cpQuantity;
        action = cpAction;
        unit = cpUnit;
        return actionDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View actionView = inflater.inflate(R.layout.paper_action_dialog, container, false) ;
        mHeader = actionView.findViewById(R.id.textView_dg_header);
        mCurrentQuantity = actionView.findViewById(R.id.textView_current_quantity);
        mAction = actionView.findViewById(R.id.textView_action_label);
        mDelta = actionView.findViewById(R.id.editText_delta);
        mNewQuantity = actionView.findViewById(R.id.textView_new_quantity);
        mOK = actionView.findViewById(R.id.button_dialog_OK);
        mCancel = actionView.findViewById(R.id.button_dialog_cancel);
        mUnit = actionView.findViewById(R.id.textView_unit);

        mCurrentQuantity.setText(String.valueOf(currentQuantity));
        mUnit.setText(unit);
        if (action.equals("in")) {
            mHeader.setText("入庫");
            mAction.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add_circle,0);
        }


        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOKClicked(view);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClicked(view);
            }
        });

        mDelta.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mDelta.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (action.equals("in")) {
                        newQuantity = currentQuantity + Integer.valueOf(mDelta.getText().toString());
                    } else if (action.equals("out")) {
                        newQuantity = currentQuantity - Integer.valueOf(mDelta.getText().toString());
                    }
                    mNewQuantity.setText(String.valueOf(newQuantity));
                    return true;
                }
                return false;
            }
        });
        return actionView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PaperActionDialogListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void onOKClicked(View view) {
        if(newQuantity == 0){
            if (action.equals("in")) {
                newQuantity = currentQuantity + Integer.valueOf(mDelta.getText().toString());
            } else if (action.equals("out")) {
                newQuantity = currentQuantity - Integer.valueOf(mDelta.getText().toString());
            }
        }
        listener.setNewQuantity(newQuantity);
        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }

}
