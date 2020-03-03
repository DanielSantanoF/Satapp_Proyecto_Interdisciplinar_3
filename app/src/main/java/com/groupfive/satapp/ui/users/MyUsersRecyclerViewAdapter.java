package com.groupfive.satapp.ui.users;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.groupfive.satapp.R;
import com.groupfive.satapp.data.repositories.UserSatAppRepository;
import com.groupfive.satapp.data.viewModel.UserViewModel;
import com.groupfive.satapp.models.auth.AuthLoginUser;

import java.util.List;

import okhttp3.ResponseBody;

public class MyUsersRecyclerViewAdapter extends RecyclerView.Adapter<MyUsersRecyclerViewAdapter.ViewHolder> {

    private final List<AuthLoginUser> mValues;
    private Context ctx;
    private UserViewModel userViewModel;
    private UserSatAppRepository userSatAppRepository;

    public MyUsersRecyclerViewAdapter(Context ctx, List<AuthLoginUser> items, UserViewModel userViewModel) {
        this.ctx = ctx;
        this.mValues = items;
        this.userViewModel = userViewModel;
        this.userSatAppRepository = new UserSatAppRepository();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.name.setText(holder.mItem.name);
        holder.email.setText(holder.mItem.email);
        holder.rol.setText(holder.mItem.role);

        if (holder.mItem.picture != null) {
            userViewModel.getPicture(holder.mItem.id).observeForever(new Observer<ResponseBody>() {
                @Override
                public void onChanged(ResponseBody responseBody) {
                    Bitmap bmp = BitmapFactory.decodeStream(responseBody.byteStream());
                    Glide.with(ctx)
                            .load(bmp)
                            .circleCrop()
                            .into(holder.foto);
                }
            });
        }else {
            Glide.with(ctx)
                    .load(R.drawable.ic_perfil)
                    .circleCrop()
                    .into(holder.foto);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView foto;
        public final TextView name;
        public final TextView email;
        public final TextView rol;
        public AuthLoginUser mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.textViewNameList);
            email = view.findViewById(R.id.textViewEmailList);
            rol = view.findViewById(R.id.textViewRolList);
            foto = view.findViewById(R.id.imageViewFotoList);

        }
    }
}
