package com.example.prm391x_project_3_fx10105;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentAlarmCreate extends Fragment {

    private static final String PERMISSION = Manifest.permission.SYSTEM_ALERT_WINDOW;
    private static final int REQUEST_CODE = 103;
    View view;
    Context mContext;

    EditText etMessage;
    Button btnSubmit;
    TextView tvTimePicker;
    String message, time;

    FragmentManager manager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_create, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etMessage = view.findViewById(R.id.et_content);
        btnSubmit = view.findViewById(R.id.btn_submit);
        tvTimePicker = view.findViewById(R.id.tv_time_picker);

        tvTimePicker.setOnClickListener(view -> TimePicker.initDateTime(view));

        btnSubmit.setOnClickListener(view -> {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.shake));

            if(!Settings.canDrawOverlays(mContext)){
                // ask for setting
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + mContext.getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                message = etMessage.getText().toString().trim();
                time = tvTimePicker.getText().toString().trim();
                if (message.equals("")) {
                    Toast.makeText(mContext, "Please fill message first!", Toast.LENGTH_SHORT).show();
                } else if (tvTimePicker.getText().toString().equals(getResources().getString(R.string.pending_time))) {
                    Toast.makeText(mContext, "Please set time first!", Toast.LENGTH_SHORT).show();
                } else {
                    if (TimePicker.calendar.getTimeInMillis() > System.currentTimeMillis()) {
                        setAlarmSchedule(message);
                        saveInfo(new Alarm(message, time, TimePicker.calendar));
                    } else {
                        saveInfo(new Alarm(message, time, TimePicker.calendar));
                    }
                    getActivity().onBackPressed();
                }
            }
        });
    }

    private void saveInfo(Alarm alarm) {
        List<Alarm> tempList = new ArrayList<>();

        String txtData = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE).getString(ActivityAlarm.KEY_DATA, null);
        if (txtData == null) {
            tempList.add(alarm);
        } else {
            Alarm[] alarmArray = new Gson().fromJson(txtData, Alarm[].class);

            tempList.addAll(Arrays.asList(alarmArray));
            tempList.add(alarm);
        }

        SharedPreferences preferences = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
        preferences.edit().putString(ActivityAlarm.KEY_DATA, new Gson().toJson(tempList)).apply();
    }

    private void setAlarmSchedule(String message) {
        long timeSet = TimePicker.calendar.getTimeInMillis();
        long timeCurrent = System.currentTimeMillis();
        long delay = timeSet - timeCurrent;

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(PendingService.TYPE, PendingService.TYPE_ALARM);
        bundle.putString(PendingService.KEY_MSG, message);
        bundle.putLong(PendingService.KEY_TIME, timeSet);

        ComponentName componentName = new ComponentName(mContext, PendingService.class);
        JobInfo info = new JobInfo.Builder(REQUEST_CODE, componentName)
                .setMinimumLatency(delay)
                .setOverrideDeadline(delay + 2000)
                .setRequiresCharging(false)
                .setExtras(bundle)
                .build();

        JobScheduler jobScheduler = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(info);

        Toast.makeText(mContext, "An Alarm will be call at: \n" +
                TimePicker.calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
    }
}