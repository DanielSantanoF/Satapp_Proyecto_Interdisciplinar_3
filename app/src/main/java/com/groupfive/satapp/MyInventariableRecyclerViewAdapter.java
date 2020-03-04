package com.groupfive.satapp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.models.inventariable.Inventariable;
import com.groupfive.satapp.retrofit.SatAppInvService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;
import com.groupfive.satapp.ui.IInventariableListener;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInventariableRecyclerViewAdapter extends RecyclerView.Adapter<MyInventariableRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private List<Inventariable> mValues;
    private final IInventariableListener mListener;
    private Context ctx;
    private SatAppInvService service;
    private String photoCode;
    private LocalDate changer;
    private Inventariable mSelected;

    public MyInventariableRecyclerViewAdapter(List<Inventariable> items, IInventariableListener listener, Context ctx) {
        this.mValues = items;
        mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_inventariable, parent, false);

        service = SatAppServiceGenerator.createService(SatAppInvService.class);

        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        changer = LocalDate.parse(holder.mItem.getCreatedAt().split("T")[0], DateTimeFormat.forPattern("yyyy-mm-dd"));

        mSelected = holder.mItem;

        holder.tvTitle.setText(holder.mItem.getNombre());

        if(holder.mItem.getDescripcion().length() > 30) {
            holder.tvDescription.setText(holder.mItem.getDescripcion().substring(0, 28) + "...");
        } else {
            holder.tvDescription.setText(holder.mItem.getDescripcion());
        }

        holder.tvDate.setText(Constants.FORMATTER.print(changer));

        photoCode = holder.mItem.getImagen().split("/")[3];
        Call<ResponseBody> call = service.getInventariableImage(photoCode);
//        Toast.makeText(ctx, photoCode, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        Glide
                                .with(ctx)
                                .load(bmp)
                                .error(Glide.with(ctx).load(R.drawable.ic_interrogation))
                                .thumbnail(Glide.with(ctx).load(Constants.LOADING_GIF))
                                .into(holder.ivPhoto);
                    } else {
                        Glide
                                .with(ctx)
                                .load(R.drawable.ic_faqs)
                                .error(Glide.with(ctx).load(R.drawable.ic_interrogation))
                                .thumbnail(Glide.with(ctx).load(Constants.LOADING_GIF))
                                .into(holder.ivPhoto);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ctx, "Error loading picture", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onClick(View v) {
        Intent i = new Intent(ctx, InvDetailActivity.class);
        i.putExtra("id", mSelected.getId());
        ctx.startActivity(i);
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
            ivPhoto = view.findViewById(R.id.imageViewType);
        }


    }
}
