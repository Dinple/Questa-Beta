package com.kris.questademo2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimePickerDialog extends DialogFragment{

    TabLayout tabLayout;
    ViewPager viewPager;

    Button btn_set;
    Button btn_cancel;

    Calendar calendar = Calendar.getInstance();

    private int year,month, day,hour, min;

    SectionsPageAdapter adapter;

    TimePickerListener timePickerListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.datetime_picker_dialog,container,false);
        tabLayout = (TabLayout) rootview.findViewById(R.id.tabs);
        viewPager = (ViewPager) rootview.findViewById(R.id.contain);
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    tab.setText(getFormatedTime(hour,min));
                }else{
                    tab.setText(getFormatedDate(month+1 ,day));
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        btn_set = rootview.findViewById(R.id.btn_set);
        btn_cancel = rootview.findViewById(R.id.btn_cancel);

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeDialog();

                if(((Main2Activity)getActivity()).timeTypeHolder.equals("unlock")){
                    checkDate(year,month, day,hour, min);
                    ((Main2Activity)getActivity()).setUnlockTime(year,month, day,hour, min);
                    String time= ((Main2Activity)getActivity()).getFormatedTime(hour,min)+" "+((Main2Activity)getActivity()).getFormatedDate(month+1,day);
                    ((Main2Activity)getActivity()).setUnlockTimeHolder("Unlock at "+ time);
                    ((Main2Activity)getActivity()).tv_preAlarm.setText(time);
                }else if(((Main2Activity)getActivity()).timeTypeHolder.equals("due")){
                    checkDate(year,month, day,hour, min);
                    ((Main2Activity)getActivity()).setDueTime(year,month, day,hour, min);
                    String time= ((Main2Activity)getActivity()).getFormatedTime(hour,min)+" "+((Main2Activity)getActivity()).getFormatedDate(month+1,day);
                    ((Main2Activity)getActivity()).setDueTimeHolder("Due at " + time);
                    ((Main2Activity)getActivity()).tv_postAlarm.setText(time);
                }



            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        return rootview;

    }


    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            return getResources().getConfiguration().locale;
        }
    }

    public String getFormatedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = h + ":" + m;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.US);
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    public String getFormatedDate(int m, int day) {
        final String OLD_FORMAT = "MMM/dd";
        final String NEW_FORMAT = "MMM.dd";

        String oldDateString = m + "/" + day;
        String newDateString = m + "." + day;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }


    public void closeDialog(){
        this.getDialog().cancel();
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(600, 600);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "TIME");
        adapter.addFragment(new Tab2Fragment(), "DATE");
        viewPager.setAdapter(adapter);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            timePickerListener = (TimePickerListener)activity;
        }catch (Exception ex){

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface TimePickerListener{
        void setReminderTime(int requestCode, Quest newQuest,String type);
    }

    public void checkDate(int year, int month, int day, int hour, int min){
        Calendar calendar = Calendar.getInstance();
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(year,month,day,hour,min);

        if(setCalendar.before(calendar) || setCalendar.equals(calendar)){
            this.day +=1;
        }
    }
}


