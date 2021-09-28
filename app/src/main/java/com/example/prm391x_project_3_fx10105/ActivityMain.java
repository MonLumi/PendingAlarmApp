package com.example.prm391x_project_3_fx10105;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ActivityMain extends AppCompatActivity implements FragmentMainMenu.IReplaceFragment {

    ImageView ivHamburger;
    DrawerLayout mDrawer;

    CardView panelSms, panelCall, panelAlarm;
    Bundle bundle;

    FragmentContainerView fragment;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

        initView();
        setPanelItemClickBehavior();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            String type = bundle.getString("TYPE");
            if (type.equals("CALL")) replaceFragment(new FragmentCall(), true);
            if (type.equals("SMS")) replaceFragment(new FragmentSMS(), true);

        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }

        else if (fragment.findViewById(R.id.fragment_main) != null){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else super.onBackPressed();
    }

    /**
     * Dung de chuyen doi cac fragment voi nhau
     * @param frg Fragment dich can chuyen toi
     * @param isFromHome neu Fragment nguon la mainFragment, thi them vao backStack. De khi an nut Back mac dinh chuyen ve trang chu
     */
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

    private void initView() {
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        ivHamburger = findViewById(R.id.iv_hamburger);
        mDrawer = findViewById(R.id.main_drawer);

        fragment = findViewById(R.id.fragment);
        manager = getSupportFragmentManager();

        manager.beginTransaction()
                .add(R.id.fragment, new FragmentMainMenu())
                .commit();

        panelAlarm = findViewById(R.id.panel_alarm);
        panelCall = findViewById(R.id.panel_call);
        panelSms = findViewById(R.id.panel_sms);
    }

    private void setPanelItemClickBehavior() {
        ivHamburger.setOnClickListener(view -> mDrawer.openDrawer(GravityCompat.START));

        panelCall.setOnClickListener(view -> {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.shake));
            replaceFragment(new FragmentCall(), fragment.findViewById(R.id.fragment_main) != null);
            mDrawer.closeDrawer(GravityCompat.START);
        });

        panelSms.setOnClickListener(view -> {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.shake));
            replaceFragment(new FragmentSMS(), fragment.findViewById(R.id.fragment_main) != null);
            mDrawer.closeDrawer(GravityCompat.START);
        });

        panelAlarm.setOnClickListener(view -> {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.shake));
            mDrawer.closeDrawer(GravityCompat.START);

            Intent intent = new Intent(ActivityMain.this, ActivityAlarm.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClicked(Fragment fragment) {
        replaceFragment(fragment, true);
    }
}