package com.app.checkinmap.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.model.Account;
import com.app.checkinmap.model.Record;
import com.app.checkinmap.model.UserLocation;
import com.app.checkinmap.util.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountAdapterList extends RecyclerView.Adapter<AccountAdapterList.AccountViewHolder>{

    private List<Account> mAccountList;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(Account account);
    }

    public AccountAdapterList(Context context,List<Account> accountList){
        mAccountList = accountList;
        mContext= context;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);

        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        holder.tvTitle.setText(mAccountList.get(position).getName());
        holder.tvPhone.setText(mAccountList.get(position).getPhone());
        holder.tvDescription.setText(mAccountList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return mAccountList.size();
    }



    class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_view_name)
        TextView tvTitle;

        @BindView(R.id.text_view_phone)
        TextView tvPhone;

        @BindView(R.id.text_view_description)
        TextView tvDescription;

        AccountViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.onItemClick(mAccountList.get(getAdapterPosition()));
            }
        }
    }
}
