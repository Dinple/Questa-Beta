package com.kris.questademo2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import java.util.Calendar;

public class Tab1Fragment extends Fragment {


    TimePicker myTimePicker;

    Calendar calendar = Calendar.getInstance();

    private int hour;
    private int min;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        myTimePicker = view.findViewById(R.id.my_timepicker);
        myTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;

                DateTimePickerDialog parentFrag = ((DateTimePickerDialog)Tab1Fragment.this.getParentFragment());
                parentFrag.setHour(hour);
                parentFrag.setMin(min);
            }
        });



        return view;
    }


    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return min;
    }
}