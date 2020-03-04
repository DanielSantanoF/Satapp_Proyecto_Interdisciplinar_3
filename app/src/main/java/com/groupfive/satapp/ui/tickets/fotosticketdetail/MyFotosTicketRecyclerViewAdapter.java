package com.groupfive.satapp.ui.tickets.fotosticketdetail;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.groupfive.satapp.R;
import com.groupfive.satapp.listeners.IFotoTicketDetailListener;

import java.util.List;


public class MyFotosTicketRecyclerViewAdapter extends RecyclerView.Adapter<MyFotosTicketRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private List<Bitmap> mValues;
    private final IFotoTicketDetailListener mListener;

    public MyFotosTicketRecyclerViewAdapter(Context context, List<Bitmap> mValues, IFotoTicketDetailListener mListener) {
        this.context = context;
        this.mValues = mValues;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fotos_ticket_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues != null){
            holder.mItem = mValues.get(position);
            Glide
                    .with(context)
                    .load(holder.mItem)
                    .error(Glide.with(context).load(R.drawable.image_not_loaded_icon))
                    .thumbnail(Glide.with(context).load(R.drawable.loading_gif))
                    .into(holder.ivFoto);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onFotoItemClick(holder.mItem);
                }
            }
        });
    }

    public void setData(List<Bitmap> list){
        this.mValues = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues != null){
            return mValues.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivFoto;
        public Bitmap mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivFoto = view.findViewById(R.id.imageViewFotoTicketDetailList);
        }

    }
}
