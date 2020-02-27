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
        holder.tvDate.setText(holder.mItem.getCreatedAt());

        //Cambiar
        Glide.with(ctx).load(holder.mItem.getImagen()).into(holder.ivPhoto);

        switch(holder.mItem.getTipo()) {
            case "PC":
                Glide.with(ctx).load(ctx.getResources().getDrawable(R.drawable.ic_ordenador_personal)).into(holder.ivType);
                break;
            case "MONITOR":
                Glide.with(ctx).load(ctx.getResources().getDrawable(R.drawable.ic_television)).into(holder.ivType);
                break;
            case "RED":
                Glide.with(ctx).load(ctx.getResources().getDrawable(R.drawable.ic_wifi)).into(holder.ivType);
                break;
            case "IMPRESORA":
                Glide.with(ctx).load(ctx.getResources().getDrawable(R.drawable.ic_herramientas_y_utensilios)).into(holder.ivType);
                break;
            case "OTRO":
                Glide.with(ctx).load(ctx.getResources().getDrawable(R.drawable.ic_desconocido)).into(holder.ivType);
                break;
            case "PERIFERICO":
                Glide.with(ctx).load(ctx.getResources().getDrawable(R.drawable.ic_teclado)).into(holder.ivType);
                break;
        }

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
        public final ImageView ivType;
        public final TextView tvDate;
        public final ImageView ivPhoto;
        public Inventariable mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvTitle = view.findViewById(R.id.textViewTitle);
            tvDescription = view.findViewById(R.id.textViewDescription);
            tvDate = view.findViewById(R.id.textViewWhen);
            ivType = view.findViewById(R.id.imageViewInventa);
            ivPhoto = view.findViewById(R.id.imageViewPhoto);
        }


    }
}
