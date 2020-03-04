package com.groupfive.satapp.ui.tickets.usertickets;

import android.content.Context;
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
import com.groupfive.satapp.data.viewModel.AllMyTicketsViewModel;
import com.groupfive.satapp.listeners.IMyTicketListener;
import com.groupfive.satapp.models.tickets.TicketModel;

import java.util.ArrayList;
import java.util.List;


public class AllMyTicketFragmentList extends Fragment {

    private int mColumnCount = 1;
    private IMyTicketListener mListener;
    Context context;
    RecyclerView recyclerView;
    List<TicketModel> myTicketList = new ArrayList<>();
    MyAllMyTicketRecyclerViewAdapter adapter;
    AllMyTicketsViewModel allMyTicketsViewModel;

    public AllMyTicketFragmentList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allMyTicketsViewModel = new ViewModelProvider(getActivity()).get(AllMyTicketsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_my_ticket_list_list, container, false);

        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyAllMyTicketRecyclerViewAdapter(context , myTicketList, mListener);
            recyclerView.setAdapter(adapter);
            loadAllMyTickets();
            Toast.makeText(context, "This are your tickets", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void loadAllMyTickets(){
        allMyTicketsViewModel.getAllMyTickets().observe(getActivity(), new Observer<List<TicketModel>>() {
            @Override
            public void onChanged(List<TicketModel> list) {
                myTicketList = list;
                adapter.setData(myTicketList);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IMyTicketListener) {
            mListener = (IMyTicketListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IMyTicketListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setVisibility(View.GONE);
        loadAllMyTickets();
        recyclerView.setVisibility(View.VISIBLE);
    }

}
