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


public class FragmentSMS extends Fragment {
    private final String PERMISSION = Manifest.permission.SEND_SMS;
    private final int REQUEST_CODE = 102;

    View view;
    Context mContext;

    EditText etPhone, etContent;
    TextView tvTime;
    Button btnSubmit;

    public FragmentSMS() {
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
        view = inflater.inflate(R.layout.fragment_sms, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView title = this.requireActivity().findViewById(R.id.tv_title);
        title.setText(R.string.sms_screen);

        etPhone = view.findViewById(R.id.et_phone_number);
        etContent = view.findViewById(R.id.et_content);
        tvTime = view.findViewById(R.id.tv_time_picker);
        btnSubmit = view.findViewById(R.id.btn_submit);

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker.initDateTime(view);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.shake));

                if (ContextCompat.checkSelfPermission(mContext, PERMISSION) ==
                        PackageManager.PERMISSION_GRANTED) {

                    String phoneNumber = etPhone.getText().toString().trim();
                    String message = etContent.getText().toString().trim();

                    if (phoneNumber.equals("") || message.equals("")) {
                        Toast.makeText(mContext, "Please input phone and message first!", Toast.LENGTH_SHORT).show();
                    } else if (tvTime.getText().equals(getResources().getString(R.string.pending_time))) {
                        Toast.makeText(mContext, "Please set time first!", Toast.LENGTH_SHORT).show();
                    } else {
                        sendMessage(phoneNumber, message);
                    }

                } else {
                    requestPermissions(new String[]{PERMISSION}, REQUEST_CODE);
                }
            }
        });


    }

    private void sendMessage(String phoneNumber, String message) {

        long timeSet = TimePicker.calendar.getTimeInMillis();
        long timeCurrent = System.currentTimeMillis();
        long delay = timeSet - timeCurrent;

        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(PendingService.TYPE, PendingService.TYPE_SMS);
        bundle.putString(PendingService.KEY_PHONE, phoneNumber);
        bundle.putString(PendingService.KEY_MSG, message);

        ComponentName serviceComponent = new ComponentName(mContext, PendingService.class);
        JobInfo info = new JobInfo.Builder(REQUEST_CODE, serviceComponent)
                .setRequiresCharging(false)
                .setMinimumLatency(delay)
                .setOverrideDeadline(delay + 2000)
                .setExtras(bundle)
                .build();

        JobScheduler jobScheduler = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(info);

        Toast.makeText(mContext, "A Message will be sent at: \n" +
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