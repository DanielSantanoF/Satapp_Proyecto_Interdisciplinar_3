package com.groupfive.satapp.ui.tickets.phototicketdetail;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.data.viewModel.TicketByIdViewModel;
import com.groupfive.satapp.listeners.IPhotoTicketDetailListener;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.retrofit.SatAppService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PhotoTicketFragmentList extends Fragment {


    private int mColumnCount = 1;
    private IPhotoTicketDetailListener mListener;
    List<String> fotosList = new ArrayList<>();
    TicketByIdViewModel ticketByIdViewModel;
    Context context;
    RecyclerView recyclerView;
    MyPhotosTicketRecyclerViewAdapter adapter;
    SatAppService service;

    public PhotoTicketFragmentList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ticketByIdViewModel = new ViewModelProvider(getActivity()).get(TicketByIdViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos_ticket_list_list, container, false);

        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyPhotosTicketRecyclerViewAdapter(context, fotosList, mListener);
            recyclerView.setAdapter(adapter);
            loadAllFotos();
        }
        return view;
    }

    public void loadAllFotos(){
        String id = MyApp.getContext().getSharedPreferences(Constants.APP_SETTINGS_FILE, Context.MODE_PRIVATE).getString("ticketId", null);
        Call<TicketModel> call = service.getTicketById(id);
        call.enqueue(new Callback<TicketModel>() {
            @Override
            public void onResponse(Call<TicketModel> call, Response<TicketModel> response) {
                fotosList = response.body().getFotos();
                adapter.setData(fotosList);
            }

            @Override
            public void onFailure(Call<TicketModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Error loading ticket", Toast.LENGTH_SHORT).show();
            }
        });
//        ticketByIdViewModel.getTicketById().observe(getActivity(), new Observer<TicketModel>() {
//            @Override
//            public void onChanged(TicketModel ticketModel) {
//                fotosList = ticketModel.getFotos();
//                adapter.setData(fotosList);
//            }
//        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IPhotoTicketDetailListener) {
            mListener = (IPhotoTicketDetailListener) context;
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


