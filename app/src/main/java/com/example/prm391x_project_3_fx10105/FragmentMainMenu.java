package com.example.prm391x_project_3_fx10105;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class FragmentMainMenu extends Fragment {

    public interface IReplaceFragment {
        void onItemClicked(Fragment fragment);
    }

    IReplaceFragment activity;

    CardView mainSms, mainCall, mainAlarm;

    View view;

    public FragmentMainMenu() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (IReplaceFragment) this.getContext();

        mainAlarm = view.findViewById(R.id.main_alarm);
        mainCall = view.findViewById(R.id.main_call);
        mainSms = view.findViewById(R.id.main_sms);

        mainAlarm.setOnClickListener(view ->{
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.shake));
            Intent intent = new Intent(this.getContext(), ActivityAlarm.class);
            startActivity(intent);
        });

        mainCall.setOnClickListener(view -> {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.shake));
            activity.onItemClicked(new FragmentCall());
        });
        mainSms.setOnClickListener(view -> {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.shake));
            activity.onItemClicked(new FragmentSMS());
        });

        TextView title = this.requireActivity().findViewById(R.id.tv_title);
        title.setText(R.string.sample_title);
    }
}