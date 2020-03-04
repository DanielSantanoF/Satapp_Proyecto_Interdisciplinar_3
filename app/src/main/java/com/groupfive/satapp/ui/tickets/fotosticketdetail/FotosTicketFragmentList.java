package com.groupfive.satapp.ui.tickets.fotosticketdetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groupfive.satapp.R;
import com.groupfive.satapp.data.viewModel.TicketByIdViewModel;
import com.groupfive.satapp.listeners.IFotoTicketDetailListener;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.retrofit.SatAppService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FotosTicketFragmentList extends Fragment {


    private int mColumnCount = 1;
    private IFotoTicketDetailListener mListener;
    List<Bitmap> fotosList;
    TicketByIdViewModel ticketByIdViewModel;
    Context context;
    RecyclerView recyclerView;
    MyFotosTicketRecyclerViewAdapter adapter;
    SatAppService service;

    public FotosTicketFragmentList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticketByIdViewModel = new ViewModelProvider(getActivity()).get(TicketByIdViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fotos_ticket_list_list, container, false);

        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyFotosTicketRecyclerViewAdapter(context, fotosList, mListener);
            recyclerView.setAdapter(adapter);
            loadAllFotos();
        }
        return view;
    }

    public void loadAllFotos(){
        ticketByIdViewModel.getTicketById().observe(getActivity(), new Observer<TicketModel>() {
            @Override
            public void onChanged(TicketModel ticketModel) {
                for (int i = 0; i < ticketModel.getFotos().size(); i++) {
                    String string = ticketModel.getFotos().get(i);
                    String[] parts = string.split("/");
                    String part1 = parts[3];
                    String part2 = parts[4];
                    Call<ResponseBody> getFotos = service.getTicketImg(part1, part2);
                    getFotos.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                                    fotosList.add(bmp);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getActivity(), "Error loading one photo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                adapter.setData(fotosList);
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFotoTicketDetailListener) {
            mListener = (IFotoTicketDetailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IFotoTicketDetailListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}


