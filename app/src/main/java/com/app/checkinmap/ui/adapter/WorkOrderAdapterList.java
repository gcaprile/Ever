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
        if(mWorkOrderList.get(position).getWorkOrderDetail().getWorkOrderAddress()!=null){
            holder.tvWorkOrderNumber.setText(mWorkOrderList.get(position).getWorkOrderDetail().getWorkOrderNumber()
            +"-"+mWorkOrderList.get(position).getWorkOrderDetail().getWorkOrderAddress().getCountry());
            holder.tvAddress.setText(mWorkOrderList.get(position).getWorkOrderDetail().getWorkOrderAddress().getAddress());
        }else{
            holder.tvWorkOrderNumber.setText(mWorkOrderList.get(position).getWorkOrderDetail().getWorkOrderNumber());
            holder.tvAddress.setText("");
        }
        if(mWorkOrderList.get(position).isIsPrincipal()){
            holder.tvPrincipal.setText("Si");
        }else{
            holder.tvPrincipal.setText("No");
        }
        holder.tvStatus.setText(mWorkOrderList.get(position).getWorkOrderDetail().getStatus());
        holder.tvAccountContact.setText(mWorkOrderList.get(position).getWorkOrderDetail().getContactName());
        holder.tvAccountName.setText(mWorkOrderList.get(position).getWorkOrderDetail().getContactAccountName());

    }

    @Override
    public int getItemCount() {
        return mWorkOrderList.size();
    }



    class WorkOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_view_work_order)
        TextView tvWorkOrderNumber;

        @BindView(R.id.text_view_principal)
        TextView tvPrincipal;

        @BindView(R.id.text_view_status)
        TextView tvStatus;

        @BindView(R.id.text_view_account_contact)
        TextView tvAccountContact;

        @BindView(R.id.text_view_account_name)
        TextView tvAccountName;

        @BindView(R.id.text_view_address)
        TextView tvAddress;

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
