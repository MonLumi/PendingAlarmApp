package com.example.prm391x_project_3_fx10105;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.util.Log;


public class PendingService extends JobService {

    public static final String TYPE = "TYPE";

    public static final String TYPE_SMS = "TYPE_SMS";
    public static final String TYPE_CALL = "TYPE_CALL";
    public static final String TYPE_ALARM = "TYPE_ALARM";

    public static final String KEY_PHONE = "KEY_PHONE";
    public static final String KEY_MSG = "KEY_MSG";
    public static final String KEY_TIME = "KEY_TIME";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        String type = jobParameters.getExtras().getString("TYPE");

        switch (type) {
            case TYPE_CALL:
                callPhone(jobParameters);
                break;

            case TYPE_SMS:
                sendSMS(jobParameters);
                break;

            case TYPE_ALARM:
                setAlarm(jobParameters);
                break;
        }
        jobFinished(jobParameters, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("Stop", "Job Stop");
        return false;
    }

    @SuppressLint("MissingPermission")
    private void sendSMS(JobParameters parameters) {
        SmsManager sms = SmsManager.getDefault();
        String phone = parameters.getExtras().getString(KEY_PHONE);
        String msg = parameters.getExtras().getString(KEY_MSG);

        sms.sendTextMessage(phone, null, msg, null, null);
    }

    @SuppressLint("MissingPermission")
    private void callPhone(JobParameters parameters) {
        //Khoi chay truc tiep tu background khong can khong qua quyen khac
        TelecomManager telecom = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        Uri address = Uri.parse(parameters.getExtras().getString(KEY_PHONE));

        telecom.placeCall(address, null);



//      Co the su dung Intent, nhung phai yeu cau quyen SYSTEM_ALERT_WINDOW
        /*
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(address);
        startActivity(intent);
        */
    }

    private void setAlarm(JobParameters parameters) {
        String message = parameters.getExtras().getString(KEY_MSG);
        long time = parameters.getExtras().getLong(KEY_TIME);

        Intent intent = new Intent(this, ActivityAlarm.class);
        intent.putExtra(KEY_MSG, message);
        intent.putExtra(KEY_TIME, time);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
