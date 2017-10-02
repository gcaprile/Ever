package com.app.checkinmap.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.model.UserLocation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountAdapterList extends RecyclerView.Adapter<AccountAdapterList.AccountViewHolder>{


    public AccountAdapterList(){

    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);

        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        holder.tvTitle.setText("Cuenta "+(position+1));

    }

    @Override
    public int getItemCount() {
        return 20;
    }



    class AccountViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_view_title)
        TextView tvTitle;

        AccountViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
