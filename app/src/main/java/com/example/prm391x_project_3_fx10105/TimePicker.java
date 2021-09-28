package com.example.prm391x_project_3_fx10105;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimePicker {

    static Calendar calendar;

    public static void initDateTime(View view) {
        calendar = Calendar.getInstance();

        new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DATE, i2);
                initTime(view);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void initTime(View view) {
        new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                calendar.set(Calendar.SECOND, 0);

                updateLabel(view);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    public static void updateLabel(View view) {
        String timeFormat = "dd/MM/YYYY HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat, Locale.CHINA);
        String time = simpleDateFormat.format(calendar.getTime());

        TextView textView = (TextView) view;
        textView.setText(time);
        textView.setTextColor(view.getContext().getColor(R.color.black));
    }

}
