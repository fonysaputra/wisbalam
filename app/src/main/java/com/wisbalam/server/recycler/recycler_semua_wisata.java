package com.wisbalam.server.recycler;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wisbalam.server.R;
import com.wisbalam.server.adapter.adapter_semua_wisata;
import com.wisbalam.server.koneksi.Server;

import java.util.ArrayList;

/**
 * Created by Fn on 11/04/2018.
 */

public class recycler_semua_wisata extends RecyclerView.Adapter<recycler_semua_wisata.RecycleViewHolder> {

    private ArrayList<adapter_semua_wisata> mRecycleList;
    private String url = Server.URL ;
    private OnItemClickListener mListener;
    Context context;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder{
        public TextView namawisata;
        public ImageView logo;



        public RecycleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            namawisata = (TextView) itemView.findViewById(R.id.tvnamawisata) ;
            logo = (ImageView) itemView.findViewById(R.id.img) ;


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!= null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });


        }
    }

    public recycler_semua_wisata(ArrayList<adapter_semua_wisata> recycleList, Context context){

        mRecycleList=recycleList;
        this.context=context;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_semua_wisata
                , parent,false);
        RecycleViewHolder rvh = new RecycleViewHolder(v,mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        adapter_semua_wisata currentItem = mRecycleList.get(position);

        holder.namawisata.setText(currentItem.getNamawisata());
        if (currentItem != null) {
            Glide.with(this.context)
                    .load(url + "/images/" + currentItem.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.logo);
        }

    }

    @Override
    public int getItemCount() {
        return mRecycleList.size();
    }




}