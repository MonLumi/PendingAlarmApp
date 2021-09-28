package com.example.prm391x_project_3_fx10105;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

public class FragmentAlarmList extends Fragment {

    Context mContext;
    View view;

    public static ArrayList<Alarm> list;

    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        loadData();

        recycler = view.findViewById(R.id.recycle_container);
        adapter = new Adapter(list, mContext);
        manager = new LinearLayoutManager(mContext);

        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(manager);

        return view;
    }

    private void loadData() {
        String txtData = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                .getString(ActivityAlarm.KEY_DATA, null);
        if (txtData == null) {
            list = new ArrayList<>();
            return;
        }

        Alarm[] alarmArray = new Gson().fromJson(txtData, Alarm[].class);
        list = new ArrayList<>(Arrays.asList(alarmArray));
        list.sort(new Comparator<Alarm>() {
            @Override
            public int compare(Alarm alarm, Alarm t1) {
                return Long.compare(alarm.getLongTime(), t1.getLongTime());
            }
        });
    }
}