package com.groupfive.satapp.ui.tickets;

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
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.data.viewModel.AddThecnicianViewModel;
import com.groupfive.satapp.listeners.IAddTechnicianListener;
import com.groupfive.satapp.models.auth.AuthLogin;
import com.groupfive.satapp.models.auth.AuthLoginUser;
import com.groupfive.satapp.models.tickets.TicketModel;

import java.util.ArrayList;
import java.util.List;

public class AddThechnicianTicketFragmentList extends Fragment {

    private int mColumnCount = 1;
    private IAddTechnicianListener mListener;
    Context context;
    RecyclerView recyclerView;
    MyAddTechnicianTicketRecyclerViewAdapter adapter;
    List<AuthLoginUser> allUsers = new ArrayList<>();
    AddThecnicianViewModel addThecnicianViewModel;

    public AddThechnicianTicketFragmentList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addThecnicianViewModel = new ViewModelProvider(getActivity()).get(AddThecnicianViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_thechnician_ticket_list_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyAddTechnicianTicketRecyclerViewAdapter(context, allUsers, mListener);
            recyclerView.setAdapter(adapter);
            Toast.makeText(MyApp.getContext(), "Slect the user to assign to the ticket", Toast.LENGTH_SHORT).show();
            loadAllUsers();
        }
        return view;
    }

    public void loadAllUsers(){
        addThecnicianViewModel.getAllUsers().observe(getActivity(), new Observer<List<AuthLoginUser>>() {
            @Override
            public void onChanged(List<AuthLoginUser> list) {
                allUsers = list;
                adapter.setData(allUsers);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IAddTechnicianListener) {
            mListener = (IAddTechnicianListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IAddTechnicianListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
