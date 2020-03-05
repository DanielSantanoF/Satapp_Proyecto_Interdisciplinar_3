package com.groupfive.satapp.ui.tickets.assignedtickets;

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

import com.groupfive.satapp.R;
import com.groupfive.satapp.data.viewModel.AssignedTicketsViewModel;
import com.groupfive.satapp.listeners.IAssignedTicketsListener;
import com.groupfive.satapp.models.tickets.TicketModel;

import java.util.ArrayList;
import java.util.List;


public class AssignedTicketFragmentList extends Fragment {

    private int mColumnCount = 1;
    private IAssignedTicketsListener mListener;
    MyAssignedTicketRecyclerViewAdapter adapter;
    Context context;
    RecyclerView recyclerView;
    List<TicketModel> assignedTickets = new ArrayList<>();
    AssignedTicketsViewModel assignedTicketsViewModel;

    public AssignedTicketFragmentList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignedTicketsViewModel = new ViewModelProvider(getActivity()).get(AssignedTicketsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assigned_ticket_list_list, container, false);

        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyAssignedTicketRecyclerViewAdapter(context, assignedTickets, mListener);
            recyclerView.setAdapter(adapter);
            loadAssignedTickets();
        }
        return view;
    }

    public void loadAssignedTickets(){
        assignedTicketsViewModel.getAssignedTickets().observe(getActivity(), new Observer<List<TicketModel>>() {
            @Override
            public void onChanged(List<TicketModel> list) {
                assignedTickets = list;
                adapter.setData(assignedTickets);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IAssignedTicketsListener) {
            mListener = (IAssignedTicketsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IAssignedTicketsListener");
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
        loadAssignedTickets();
        recyclerView.setVisibility(View.VISIBLE);
    }


}
