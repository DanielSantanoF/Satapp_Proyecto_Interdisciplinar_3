package com.groupfive.satapp.ui.tickets;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupfive.satapp.R;
import com.groupfive.satapp.listeners.IAddTechnicianListener;
import com.groupfive.satapp.models.auth.AuthLogin;
import com.groupfive.satapp.models.auth.AuthLoginUser;

import java.util.List;


public class MyAddTechnicianTicketRecyclerViewAdapter extends RecyclerView.Adapter<MyAddTechnicianTicketRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private List<AuthLoginUser> mValues;
    private final IAddTechnicianListener mListener;

    public MyAddTechnicianTicketRecyclerViewAdapter(Context context, List<AuthLoginUser> mValues, IAddTechnicianListener mListener) {
        this.context = context;
        this.mValues = mValues;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_add_thechnician_ticket_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues != null){
            holder.mItem = mValues.get(position);
            holder.txtName.setText(holder.mItem.getName());
            holder.txtRole.setText(holder.mItem.getRole());
            holder.txtContact.setText(holder.mItem.getEmail());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onAllTicketsItemClick(holder.mItem);
                    }
                }
            });
        }
    }

    public void setData(List<AuthLoginUser> list){
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
        public final TextView txtName;
        public final TextView txtRole;
        public final TextView txtContact;
        public AuthLoginUser mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtName = view.findViewById(R.id.textViewAddTechnicianName);
            txtRole = view.findViewById(R.id.textViewAddTechnicianRole);
            txtContact = view.findViewById(R.id.textViewAddTechnicianContact);
        }

    }
}