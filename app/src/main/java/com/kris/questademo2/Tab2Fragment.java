package com.kris.questademo2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import java.util.Calendar;

public class Tab2Fragment extends Fragment {
    Calendar calendar = Calendar.getInstance();

    private DatePicker myDatePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);
        myDatePicker = view.findViewById(R.id.my_datepicker);
        myDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateTimePickerDialog parentFrag = ((DateTimePickerDialog)Tab2Fragment.this.getParentFragment());
                parentFrag.setYear(2018);
                parentFrag.setMonth(monthOfYear);
                parentFrag.setDay(dayOfMonth);
            }
        });

        return view;
    }

}

