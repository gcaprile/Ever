package com.app.checkinmap.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.model.Record;
import com.app.checkinmap.model.UserLocation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountAdapterList extends RecyclerView.Adapter<AccountAdapterList.AccountViewHolder>{

    private List<Record> mRecordList;

    public AccountAdapterList(List<Record> recordList){
        mRecordList = recordList;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);

        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        holder.tvTitle.setText(mRecordList.get(position).getName());
        holder.tvDescription.setText(mRecordList.get(position).getAttributes().getUrl());
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }



    class AccountViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_view_title)
        TextView tvTitle;

        @BindView(R.id.text_view_description)
        TextView tvDescription;

        AccountViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
