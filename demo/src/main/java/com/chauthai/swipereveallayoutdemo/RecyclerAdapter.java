package com.chauthai.swipereveallayoutdemo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hydrated.swipereveallayout.SwipeListener;
import com.hydrated.swipereveallayout.SwipeRevealLayout;
import com.hydrated.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chau Thai on 4/8/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter implements SwipeListener {
    private List<String> mDataSet = new ArrayList<>();
    private LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    private static final String tag = "SwipeDemo";


    public RecyclerAdapter(Context context, List<String> dataSet) {
        mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);

        // uncomment if you want to open only one row at a time
        // binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final ViewHolder holder = (ViewHolder) h;

        if (mDataSet != null && 0 <= position && position < mDataSet.size()) {
            final String data = mDataSet.get(position);

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipeLayout, data);

            // Bind your data here
            holder.bind(data, position);
        }

    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    @Override
    public void onClosed(SwipeRevealLayout view) {
    }

    @Override
    public void onOpened(SwipeRevealLayout view, int dragEdge) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        if (dragEdge == SwipeRevealLayout.DRAG_EDGE_LEFT)
            holder.deleteLayout.performClick();
    }

    @Override
    public void onSlide(SwipeRevealLayout view, float slideOffset) {
    }

    @Override
    public void onTouchUp(boolean isUp) {
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private View deleteLayout;
        private TextView textView;
        private TextView fake;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
            swipeLayout.setSwipeListener(RecyclerAdapter.this);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            textView = (TextView) itemView.findViewById(R.id.text);
            fake = (TextView) itemView.findViewById(R.id.fake);
        }

        public void bind(String data, int position) {
            swipeLayout.setTag(this);
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDataSet.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            textView.setText(data);
            fake.setVisibility((position % 2) == 0 ? View.VISIBLE : View.GONE);
        }
    }
}
