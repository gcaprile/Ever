package com.app.checkinmap.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.model.Lead;
import com.app.checkinmap.model.WorkOrder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkOrderAdapterList extends RecyclerView.Adapter<WorkOrderAdapterList.WorkOrderViewHolder>{

    private List<WorkOrder> mWorkOrderList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(WorkOrder lead);
    }

    public WorkOrderAdapterList(List<WorkOrder> leadList){
        mWorkOrderList = leadList;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public WorkOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_order, parent, false);

        return new WorkOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkOrderViewHolder holder, int position) {
        holder.tvTitle.setText(mWorkOrderList.get(position).getWorkOrderNumber());
        holder.tvSubTitle.setText(mWorkOrderList.get(position).getStatus());
        holder.tvDescription.setText(mWorkOrderList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mWorkOrderList.size();
    }



    class WorkOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_view_title)
        TextView tvTitle;

        @BindView(R.id.text_view_sub_title)
        TextView tvSubTitle;

        @BindView(R.id.text_view_description)
        TextView tvDescription;

        WorkOrderViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.onItemClick(mWorkOrderList.get(getAdapterPosition()));
            }
        }
    }
}
