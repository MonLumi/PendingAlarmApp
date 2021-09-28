package com.example.prm391x_project_3_fx10105;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentCall extends Fragment {

    public final String PERMISSION = Manifest.permission.CALL_PHONE;
    public final int REQUEST_CODE = 101;
    private Context mContext;

    EditText etPhoneNumber;
    TextView tvTimePicker;
    Button btnSubmit;
    View view;

    String phoneNumber;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_call, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView title = this.requireActivity().findViewById(R.id.tv_title);
        title.setText(R.string.call_screen);

        etPhoneNumber = view.findViewById(R.id.et_phone_number);

        tvTimePicker = view.findViewById(R.id.tv_time_picker);
        tvTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker.initDateTime(view);
            }
        });

        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake));

                if (ContextCompat.checkSelfPermission(mContext, PERMISSION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    
                    phoneNumber = etPhoneNumber.getText().toString().trim();

                    if (phoneNumber.equals("")) {
                        Toast.makeText(mContext, "Please fill phone number first!", Toast.LENGTH_SHORT).show();
                    } else if (tvTimePicker.getText().equals(getResources().getString(R.string.pending_time))) {
                        Toast.makeText(mContext, "Please check setup time first!", Toast.LENGTH_SHORT).show();
                    } else {
                        setCallSchedule(phoneNumber);
                    }

                } else {
                    requestPermissions(new String[] {PERMISSION}, REQUEST_CODE);
                }
            }
        });

    }
    
    private void setCallSchedule(String phoneNumber) {

        long timeSet = TimePicker.calendar.getTimeInMillis();
        long timeCurrent = System.currentTimeMillis();
        long delay = timeSet - timeCurrent;

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(PendingService.TYPE, PendingService.TYPE_CALL);
        bundle.putString(PendingService.KEY_PHONE, "tel:" + phoneNumber);

        ComponentName componentName = new ComponentName(mContext, PendingService.class);
        JobInfo info = new JobInfo.Builder(REQUEST_CODE, componentName)
                .setMinimumLatency(delay)
                .setOverrideDeadline(delay + 2000)
                .setRequiresCharging(false)
                .setExtras(bundle)
                .build();

        JobScheduler jobScheduler = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(info);

        Toast.makeText(mContext, "A Call will be done at: \n" +
                TimePicker.calendar.getTime().toString(), Toast.LENGTH_SHORT).show();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new FragmentMainMenu())
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "Permission Granted, Thank you!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Please allow permission for using it", Toast.LENGTH_SHORT).show();
        }
    }
}