package com.app.checkinmap.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.model.Account;
import com.app.checkinmap.model.Lead;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeadAdapterList extends RecyclerView.Adapter<LeadAdapterList.AccountViewHolder>{

    private List<Lead> mLeadList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(Lead lead);
    }

    public LeadAdapterList(List<Lead> leadList){
        mLeadList = leadList;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lead, parent, false);

        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        holder.tvName.setText(mLeadList.get(position).getName());
        holder.tvCompany.setText(mLeadList.get(position).getCompany());
        holder.tvPhone.setText(mLeadList.get(position).getPhone());
        holder.tvAddress.setText(mLeadList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return mLeadList.size();
    }



    class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_view_name)
        TextView tvName;

        @BindView(R.id.text_view_company)
        TextView tvCompany;

        @BindView(R.id.text_view_phone)
        TextView tvPhone;

        @BindView(R.id.text_view_address)
        TextView tvAddress;

        AccountViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.onItemClick(mLeadList.get(getAdapterPosition()));
            }
        }
    }
}
