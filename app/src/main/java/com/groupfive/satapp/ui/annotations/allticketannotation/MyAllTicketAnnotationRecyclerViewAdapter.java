package com.groupfive.satapp.ui.annotations.allticketannotation;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.groupfive.satapp.R;
import com.groupfive.satapp.listeners.ITicketAnnotationListener;
import com.groupfive.satapp.models.tickets.TicketAnotaciones;

import java.util.List;

public class MyAllTicketAnnotationRecyclerViewAdapter extends RecyclerView.Adapter<MyAllTicketAnnotationRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private List<TicketAnotaciones> mValues;
    private final ITicketAnnotationListener mListener;

    public MyAllTicketAnnotationRecyclerViewAdapter(Context context, List<TicketAnotaciones> mValues, ITicketAnnotationListener mListener) {
        this.context = context;
        this.mValues = mValues;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_all_ticket_annotation_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.txtCuerpo.setText(holder.mItem.getCuerpo());
        Glide
                .with(context)
                .load(context.getResources().getDrawable(R.drawable.icon_annotation))
                .into(holder.ivAnnotation);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onFotoItemClick(holder.mItem);
                }
            }
        });
    }

    public void setData(List<TicketAnotaciones> list){
        this.mValues = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtCuerpo;
        public final ImageView ivAnnotation;
        public final ImageButton btnEdit;
        public final ImageButton btnDelete;
        public TicketAnotaciones mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtCuerpo = view.findViewById(R.id.textViewAnnotationTicketDetail);
            ivAnnotation = view.findViewById(R.id.imageViewAnnotationTicketetail);
        }

    }
}
