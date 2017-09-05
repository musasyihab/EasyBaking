package com.musasyihab.easybaking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.musasyihab.easybaking.R;
import com.musasyihab.easybaking.model.StepModel;

import java.util.List;

/**
 * Created by musasyihab on 9/3/17.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private static final String TAG = StepAdapter.class.getSimpleName();
    private List<StepModel> stepList;
    private final StepAdapterClickListener mOnClickListener;
    private Context context;

    public interface StepAdapterClickListener {
        void onStepClick(StepModel stepClicked, int clickedPosition);
    }

    public StepAdapter (Context context, List<StepModel> stepList, StepAdapterClickListener listener) {
        this.context = context;
        this.stepList = stepList;
        mOnClickListener = listener;

    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        StepViewHolder viewHolder = new StepViewHolder(view);

        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: ");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(stepList.get(position));
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView stepName;

        public StepViewHolder(View itemView) {
            super(itemView);

            stepName = (TextView) itemView.findViewById(R.id.step_item_name);
            itemView.setOnClickListener(this);
        }

        void bind(StepModel step) {
            if(step!=null) {
                stepName.setText((getAdapterPosition() + 1) + ". " + step.getShortDescription());
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onStepClick(stepList.get(clickedPosition), clickedPosition);
        }
    }
}
