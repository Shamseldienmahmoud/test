package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.StationsItem;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collection;

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.ViewHoder>implements Filterable {
    Context context;
    ArrayList<StationsItem> itemsArray;
    ArrayList<StationsItem>allstations;

    public StationsAdapter(Context context, ArrayList<StationsItem> itemsArray) {
        this.context = context;
        this.itemsArray = itemsArray;
        this.allstations = new ArrayList<>(itemsArray);

    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customitem,null);
        return new ViewHoder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, final int position) {
        holder.stationname.setText(itemsArray.get(position).getStationName());
        holder.linenme.setText(String.valueOf(itemsArray.get(position).getLineName()));

    }
    @Override
    public int getItemCount() {
        return itemsArray.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<StationsItem>filterdStaions=new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filterdStaions.addAll(allstations);
            }else {
                for (StationsItem station :allstations){
                    if (station.getStationName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filterdStaions.add(station);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values = filterdStaions;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
          itemsArray.clear();
           itemsArray.addAll((Collection<? extends StationsItem>) results.values);
           notifyDataSetChanged();
        }
    };

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView stationname,linenme;
        public ViewHoder(@NonNull final View itemView) {
            super(itemView);
            stationname=itemView.findViewById(R.id.stationname);
            linenme=itemView.findViewById(R.id.linename);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String  s = itemsArray.get(getAdapterPosition()).getStationName();
                    String n=String.valueOf(itemsArray.get(getAdapterPosition()).getId());
                    Intent intent = new Intent();
                    intent.putExtra("start_station", s);
                    intent.putExtra("end_station",s);
                    intent.putExtra("station_id",n);
                    if (context instanceof Activity)
                        ((Activity) context).setResult(Activity.RESULT_OK, intent);
                    ((Activity)context).finish();
                }
            });

        }
    }


}
