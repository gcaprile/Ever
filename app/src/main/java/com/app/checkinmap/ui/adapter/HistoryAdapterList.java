package com.app.checkinmap.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.app.checkinmap.R;
import com.app.checkinmap.db.DatabaseManager;
import com.app.checkinmap.model.CheckPointLocation;
import com.app.checkinmap.model.UserLocation;
import com.app.checkinmap.util.PreferenceManager;
import com.app.checkinmap.util.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapterList extends RecyclerView.Adapter<HistoryAdapterList.HistoryViewHolder>{

    private List<CheckPointLocation> mLocationsList;
    private Context                  mContext;

    public HistoryAdapterList(List<CheckPointLocation> locationList,Context context){
        this.mLocationsList = locationList;
        this.mContext = context;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        String location = String.valueOf(mLocationsList.get(position).getCheckInLatitude()) + ", " + String.valueOf(mLocationsList.get(position).getCheckInLongitude());
        holder.tvCoordinates.setText(location);
        holder.tvAddress.setText(mLocationsList.get(position).getAddress());
        holder.tvAccountName.setText(mLocationsList.get(position).getAccountContactName());
        holder.tvStartTime.setText(getTimeFromDate(mLocationsList.get(position).getCheckInDate()));
        holder.tvFinishTime.setText(getTimeFromDate(mLocationsList.get(position).getCheckOutDate()));
        holder.tvVisitName.setText(mLocationsList.get(position).getName());
        holder.tvVisitTime.setText(Utility.getDurationInHours(mLocationsList.get(position).getCheckInDate(),
                mLocationsList.get(position).getCheckOutDate()));
        if(position==0){
            String routeStartDate = DatabaseManager.getInstance().getRouteStartDate(PreferenceManager.getInstance(mContext).getRouteId());
            holder.tvRouteTime.setText(Utility.getDurationInHours(routeStartDate,mLocationsList.get(position).getCheckInDate()));
        }else{
            holder.tvRouteTime.setText(Utility.getDurationInHours(mLocationsList.get(position-1).getCheckOutDate(),mLocationsList.get(position).getCheckInDate()));
        }
    }

    @Override
    public int getItemCount() {
        return mLocationsList.size();
    }

    private  String getTimeFromDate(String stringDate){
        String dateOut = "";
        try {
            DateFormat srcDf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            // parse the date string into Date object
            Date date = srcDf.parse(stringDate);

            DateFormat destDf = new SimpleDateFormat("hh:mm:ss a");

            // format the date into another format
            dateOut = destDf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOut;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_view_coordinate)
        TextView tvCoordinates;

        @BindView(R.id.text_view_address)
        TextView tvAddress;

        @BindView(R.id.text_view_account_name)
        TextView tvAccountName;

        @BindView(R.id.text_view_start_time)
        TextView tvStartTime;

        @BindView(R.id.text_view_finish_time)
        TextView tvFinishTime;

        @BindView(R.id.text_view_visit_time)
        TextView tvVisitTime;

        @BindView(R.id.text_view_route_time)
        TextView tvRouteTime;

        @BindView(R.id.text_view_visit_name)
        TextView tvVisitName;

        HistoryViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
