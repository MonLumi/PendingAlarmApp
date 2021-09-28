package com.example.prm391x_project_3_fx10105;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

public class ActivityAlarm extends AppCompatActivity implements View.OnClickListener, Adapter.IAlarmList {

    public static final String KEY_DATA = "KEY_DATA";


    ImageView ivHamburger;
    DrawerLayout mDrawer;
    CardView panelAlarm, panelCall, panelSms;

    FragmentContainerView fragment;
    FragmentManager manager;

    ImageView addAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        initView();
        initListFrag();
        Intent intent = getIntent();
        if (intent.getStringExtra(PendingService.KEY_MSG) != null) {
            initScreenFrag();
        }

    }

    private void initView() {
        mDrawer = findViewById(R.id.main_drawer);
        ivHamburger = findViewById(R.id.iv_hamburger);
        ivHamburger.setOnClickListener(view -> mDrawer.openDrawer(GravityCompat.START));



        panelAlarm = findViewById(R.id.panel_alarm);
        panelCall = findViewById(R.id.panel_call);
        panelSms = findViewById(R.id.panel_sms);

        panelAlarm.setOnClickListener(this);
        panelCall.setOnClickListener(this);
        panelSms.setOnClickListener(this);

        addAlarm = findViewById(R.id.iv_add_alarm);

        addAlarm.setOnClickListener(view -> replaceFragment(new FragmentAlarmCreate(), true));
    }

    private void initListFrag() {
        fragment = findViewById(R.id.fragment);
        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment, new FragmentAlarmList())
                .commit();
    }

    private void initScreenFrag() {
        fragment = findViewById(R.id.fragment);
        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment, new FragmentAlarmScreen())
                .addToBackStack(null)
                .commit();
        Log.d("initScreen", "Done");
    }

    public void replaceFragment(Fragment frg, boolean isFromHome) {
        if (isFromHome) {
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.fragment, frg)
                    .addToBackStack(null)
                    .commit();

        } else {
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.fragment, frg)
                    .commit();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == panelAlarm) {
            Toast.makeText(this, "Do it later", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(ActivityAlarm.this, ActivityMain.class);
            Bundle bundle = new Bundle();
            if (view == panelCall) {
                bundle.putString("TYPE", "CALL");
            } else {
                bundle.putString("TYPE", "SMS");
            }

            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment.findViewById(R.id.recycle_container) != null) {
            finish();
        } else
            super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public void onAlarmClicked(int index) {
        initDialog(index);
    }

    public void initDialog(int index) {
        //init layout
        LayoutInflater inflater = getLayoutInflater();
        View alertDialog = inflater.inflate(R.layout.my_dialog, null);
        Alarm alarm = FragmentAlarmList.list.get(index);
        TextView message = alertDialog.findViewById(R.id.tv_message);
        message.setText(alarm.getTitle());

        //parse layout to dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setView(alertDialog);
        AlertDialog dialog = alert.create();

        //set layout button behavior
        alertDialog.findViewById(R.id.btn_done).setOnClickListener(view -> {
            updateDone(index);
            dialog.dismiss();
        });
        alertDialog.findViewById(R.id.btn_remove).setOnClickListener(view -> {
            updateRemove(index);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateRemove(int index) {
        FragmentAlarmList.list.remove(index);
        SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        preferences.edit().putString(ActivityAlarm.KEY_DATA, new Gson().toJson(FragmentAlarmList.list)).apply();

        manager.beginTransaction()
                .replace(R.id.fragment, new FragmentAlarmList())
                .commit();
    }

    private void updateDone(int index) {
        FragmentAlarmList.list.get(index).setStatus("done");

        SharedPreferences preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        preferences.edit().putString(ActivityAlarm.KEY_DATA, new Gson().toJson(FragmentAlarmList.list)).apply();

        manager.beginTransaction()
                .replace(R.id.fragment, new FragmentAlarmList())
                .commit();
    }
}