package com.groupfive.satapp.ui.tickets.ticketdetail;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FotosTicketDetailActivity extends AppCompatActivity {

    String ticketId;
    SatAppService service;
    LinearLayout layout;
    ImageView image;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos_ticket_detail);

        ticketId = getIntent().getExtras().get(Constants.EXTRA_TICKET_ID).toString();

        layout = findViewById(R.id.linearLayoutDinamicFotosTicketDetail);
        //layout.removeView();

        loadFotos();
    }

    public void loadFotos(){
        service = SatAppServiceGenerator.createService(SatAppService.class);
        Call<TicketModel> call = service.getTicketById(ticketId);
        call.enqueue(new Callback<TicketModel>() {
            @Override
            public void onResponse(Call<TicketModel> call, Response<TicketModel> response) {
                for (i = 0; i <response.body().getFotos().size() ; i++) {
                    String string = response.body().getFotos().get(i);
                    String[] parts = string.split("/");
                    String part1 = parts[3];
                    String part2 = parts[4];

                    //String myDynamicalyCreatedName = "name" + Utils.rndRng(0, 100);

                    image = new ImageView(FotosTicketDetailActivity.this);
                    //image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));

                    Call<ResponseBody> getFotos = service.getTicketImg(part1, part2);
                    getFotos.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                                    Glide
                                            .with(FotosTicketDetailActivity.this)
                                            .load(bmp)
                                            .error(Glide.with(FotosTicketDetailActivity.this).load(R.drawable.image_not_loaded_icon))
                                            .thumbnail(Glide.with(FotosTicketDetailActivity.this).load(R.drawable.loading_gif))
                                            .into(image);
                                }
                            } else {
                                Glide
                                        .with(FotosTicketDetailActivity.this)
                                        .load(R.drawable.image_not_loaded_icon)
                                        .error(Glide.with(FotosTicketDetailActivity.this).load(R.drawable.image_not_loaded_icon))
                                        .thumbnail(Glide.with(FotosTicketDetailActivity.this).load(R.drawable.loading_gif))
                                        .into(image);
                            }
                            layout.addView(image);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(FotosTicketDetailActivity.this, "Error loading one photo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TicketModel> call, Throwable t) {
                Toast.makeText(FotosTicketDetailActivity.this, "This ticket dont have any photo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
