package com.groupfive.satapp.ui.tickets.assignedtickets;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.data.viewModel.AssignedTicketsViewModel;
import com.groupfive.satapp.listeners.IAssignedTicketsListener;
import com.groupfive.satapp.models.calendar.CalendarModel;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.ui.datepicker.DialogDatePickerFragment;
import com.groupfive.satapp.ui.datepicker.IDatePickerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AssignedTicketFragmentList extends Fragment implements IDatePickerListener {

    private int mColumnCount = 1;
    private IAssignedTicketsListener mListener;
    MyAssignedTicketRecyclerViewAdapter adapter;
    Context context;
    RecyclerView recyclerView;
    List<TicketModel> assignedTickets = new ArrayList<>();
    AssignedTicketsViewModel assignedTicketsViewModel;
    Activity activity;
    //CALENDAR
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    public static final int REQUEST_READ_CALENDAR = 79;
    public static final int REQUEST_WRITE_CALENDAR = 78;
    Uri uri;
    private static final String DEBUG_TAG = "AssignedTicketsFragmentList";
    List<CalendarModel> calendarModelList = new ArrayList<>();

    public AssignedTicketFragmentList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignedTicketsViewModel = new ViewModelProvider(getActivity()).get(AssignedTicketsViewModel.class);
        activity = (Activity)context;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_assigned_tickets, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_all_to_calendar:
                //TODO CHECK IT ALL ARE ADDED WITH A GOOGLE ACC
                requestPermissionReadCalendar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.OWNER_ACCOUNT
    };

    private void requestPermissionReadCalendar() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALENDAR)) {
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR},
                    REQUEST_READ_CALENDAR);
        }
    }

    private void requestPermissionWriteCalendar() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CALENDAR)) {
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR},
                    REQUEST_WRITE_CALENDAR);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_CALENDAR: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DialogFragment datePickerFragment = DialogDatePickerFragment.newInstance(activity);
                    datePickerFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "datePicker");
                    Toast.makeText(activity, getResources().getString(R.string.date_all_tickets_add), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity,  getResources().getString(R.string.calendar_permission_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_WRITE_CALENDAR: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.calendar_permission_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    @Override
    public void onDateSelected(int year, int month, int day) {
        addTicketToCalendar(day, month+1, year);
    }

    public void addTicketToCalendar(int day, int month, int year) {
        Cursor cur = null;
        ContentResolver cr = activity.getContentResolver();
        uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String userEmail = MyApp.getContext().getSharedPreferences(Constants.APP_SETTINGS_FILE, Context.MODE_PRIVATE).getString(Constants.SHARED_PREFERENCES_EMAIL, null);
        String[] selectionArgs = new String[]{userEmail, "com.google", userEmail};
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            calendarModelList.add(new CalendarModel(calID, displayName, accountName, ownerName));
        }
        long calID = calendarModelList.get(0).getCalID();
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day);// ,0 ,0
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, day);
        endMillis = endTime.getTimeInMillis();

        for (int i = 0; i <assignedTickets.size() ; i++) {
            ContentResolver contentResolver = activity.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, assignedTickets.get(i).getTitulo());
            values.put(CalendarContract.Events.DESCRIPTION, assignedTickets.get(i).getDescripcion());
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Madrid/Spain");
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionWriteCalendar();
                return;
            }
            Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
        }
        Toast.makeText(activity, getResources().getString(R.string.calendar_all_events_added), Toast.LENGTH_SHORT).show();
    }


}
