package com.will.basketballrecorder;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by willhennessy on 4/5/16.
 */
public class EventLabelDialogFragment extends DialogFragment {

    private OnEventLabelledListener mListener;
    private float x;
    private float y;

    static EventLabelDialogFragment newInstance() {
        EventLabelDialogFragment f = new EventLabelDialogFragment();
        f.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.EventLabelFragmentTheme);
        return f;
    }

    public void init(OnEventLabelledListener listener, float x, float y) {
        this.mListener = listener;
        this.x = x;
        this.y = y;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.event_label_dialog_fragment, container, false);
        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = ((Button) v).getText().toString();
                mListener.onEventLabelled(action, x, y);
                dismiss();
            }
        };
        v.findViewById(R.id.button_score).setOnClickListener(btnListener);
        v.findViewById(R.id.button_miss).setOnClickListener(btnListener);
        v.findViewById(R.id.button_assist).setOnClickListener(btnListener);
        v.findViewById(R.id.button_foul).setOnClickListener(btnListener);
        v.findViewById(R.id.button_rebound).setOnClickListener(btnListener);
        v.findViewById(R.id.button_steal).setOnClickListener(btnListener);
        return v;
    }

}
