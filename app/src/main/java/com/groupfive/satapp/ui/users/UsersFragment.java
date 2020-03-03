package com.groupfive.satapp.ui.users;

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
import com.groupfive.satapp.data.viewModel.UserViewModel;
import com.groupfive.satapp.models.auth.AuthLoginUser;
import com.groupfive.satapp.ui.users.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private Context context;
    private RecyclerView users , validated;
    private UserViewModel userViewModel;
    private List<AuthLoginUser> listUsers, listValidates;
    private MyUsersRecyclerViewAdapter adapterUser, adapterValidate;

    public UsersFragment() {
    }

    public static UsersFragment newInstance(int columnCount) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);
        users = view.findViewById(R.id.listUsers);
        validated = view.findViewById(R.id.listValidated);
        context = view.getContext();

        loadUser();
        loadValidated();

        return view;
    }

    public void loadUser(){
        listUsers = new ArrayList<>();
        if (mColumnCount <= 1) {
            users.setLayoutManager(new LinearLayoutManager(context));
        } else {
            users.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapterUser = new MyUsersRecyclerViewAdapter(
                context,
                listUsers,
                userViewModel
        );
        users.setAdapter(adapterUser);

        userViewModel.getAllUser().observe(getActivity(), new Observer<List<AuthLoginUser>>() {
            @Override
            public void onChanged(List<AuthLoginUser> authLoginUsers) {
                listUsers.addAll(authLoginUsers);
                adapterUser.notifyDataSetChanged();
            }
        });
    }

    public void loadValidated(){
        listValidates = new ArrayList<>();
        if (mColumnCount <= 1) {
            validated.setLayoutManager(new LinearLayoutManager(context));
        } else {
            validated.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        adapterValidate = new MyUsersRecyclerViewAdapter(
                context,
                listValidates,
                userViewModel
        );

        validated.setAdapter(adapterValidate);

        userViewModel.getUsersValidated().observe(getActivity(), new Observer<List<AuthLoginUser>>() {
            @Override
            public void onChanged(List<AuthLoginUser> authLoginUsers) {
                listValidates.addAll(authLoginUsers);
                adapterValidate.notifyDataSetChanged();
            }
        });
    }
}
