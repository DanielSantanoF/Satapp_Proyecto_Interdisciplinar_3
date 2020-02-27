package com.groupfive.satapp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.groupfive.satapp.models.inventariable.Inventariable;
import com.groupfive.satapp.ui.IInventariableListener;

import java.util.List;

public class MyInventariableRecyclerViewAdapter extends RecyclerView.Adapter<MyInventariableRecyclerViewAdapter.ViewHolder> {

    private List<Inventariable> mValues;
    private final IInventariableListener mListener;
    private Context ctx;

    public MyInventariableRecyclerViewAdapter(List<Inventariable> items, IInventariableListener listener, Context ctx) {
        this.mValues = items;
        mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_inventariable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.tvTitle.setText(holder.mItem.getNombre());
        holder.tvDescription.setText(holder.mItem.getDescripcion());
        holder.tvType.setText(holder.mItem.getTipo());
        holder.tvDate.setText(holder.mItem.getCreatedAt());

        //Cambiar
        Glide.with(ctx).load(holder.mItem.getImagen()).into(holder.ivPhoto);

    }

    public void setData(List<Inventariable> result) {
        this.mValues = result;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues != null) {
            return mValues.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvTitle;
        public final TextView tvDescription;
        public final TextView tvType;
        public final TextView tvDate;
        public final ImageView ivPhoto;
        public Inventariable mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvTitle = view.findViewById(R.id.textViewTitle);
            tvDescription = view.findViewById(R.id.textViewDescription);
            tvDate = view.findViewById(R.id.textViewWhen);
            tvType = view.findViewById(R.id.textViewType);
            ivPhoto = view.findViewById(R.id.imageViewPhoto);
        }


    }
}
