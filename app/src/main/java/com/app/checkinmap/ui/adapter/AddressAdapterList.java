package com.app.checkinmap.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.checkinmap.R;
import com.app.checkinmap.model.Record;
import com.app.checkinmap.util.PreferenceManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressAdapterList extends RecyclerView.Adapter<AddressAdapterList.AddressViewHolder>{

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(String selection);
    }

    public AddressAdapterList(){
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);

        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        holder.tvTitle.setText("Dirección "+(position+1));
        holder.tvDescription.setText(R.string.address_detail);
    }

    @Override
    public int getItemCount() {
        return 10;
    }



    class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text_view_title)
        TextView tvTitle;

        @BindView(R.id.text_view_description)
        TextView tvDescription;

        AddressViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.onItemClick(tvTitle.getText().toString());
            }
        }
    }
}
