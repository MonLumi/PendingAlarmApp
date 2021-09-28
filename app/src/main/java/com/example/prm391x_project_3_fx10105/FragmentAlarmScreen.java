package com.example.prm391x_project_3_fx10105;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class FragmentAlarmScreen extends Fragment {
    View view;
    Context mContext;

    TextView tvMessage;
    Button btnStop, btnDone;
    String message;
    long timeInMilis;

    public FragmentAlarmScreen() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_screen, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        message = getActivity().getIntent().getStringExtra(PendingService.KEY_MSG);
        timeInMilis = getActivity().getIntent().getLongExtra(PendingService.KEY_TIME, 0);
        Log.d("Alarm Screen", String.valueOf(timeInMilis));

        tvMessage = view.findViewById(R.id.tv_content);
        tvMessage.setText(message);

        btnStop = view.findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                view1.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake));

                updateAlarmStatus(timeInMilis, false);
                getActivity().onBackPressed();
            }
        });

        btnDone = view.findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake));

                updateAlarmStatus(timeInMilis, true);
                getActivity().onBackPressed();
            }
        });
    }

    private void updateAlarmStatus(long timeInMilis, boolean isDone) {
        String txtData = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
                                        .getString(ActivityAlarm.KEY_DATA, null);
        if (txtData == null) {
            return;
        }

        Alarm[] alarmArray = new Gson().fromJson(txtData, Alarm[].class);

        for (Alarm alarm : alarmArray) {
            if (alarm.getLongTime() == timeInMilis) {
                alarm.setStatus((isDone) ? "done" : "stopped");
                break;
            }
        }

        mContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
                .edit()
                .putString(ActivityAlarm.KEY_DATA, new Gson().toJson(alarmArray, Alarm[].class))
                .apply();
    }
}