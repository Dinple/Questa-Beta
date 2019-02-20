package com.kris.questademo2;

import java.util.Calendar;


public class Quest {
    private Calendar ulTime;
    private Calendar duTime;
    private int requestCode;
    private String detail;
    private boolean isDue;
    private boolean isLock;

    public Quest(int requestCode, int year,int month, int day, int hour, int minute, String detail,boolean isLock,boolean isDue){
        this.requestCode = requestCode;
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(year,month,day,hour,minute);

        this.detail = detail;
        this.isLock = isLock;
        this.isDue = isDue;
        if(isLock){
            ulTime = setCalendar;
        }

        if(isDue){
            duTime = setCalendar;
        }
    }

    public Quest(int requestCode, int ulYear,int ulMonth, int ulDay, int ulHour, int ulMinute, int duYear, int duMonth, int duDay, int duHour, int duMinute,String detail){
        this.requestCode = requestCode;
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(ulYear,ulMonth,ulDay,ulHour,ulMinute);

        ulTime = setcalendar;
        setcalendar.set(duYear,duMonth,duDay,duHour,duMinute);

        duTime = setcalendar;
        this.detail = detail;
        this.isDue = true;
        this.isLock = true;
    }

    public void setDue(boolean due) {
        isDue = due;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public Calendar getDuTime() {
        return duTime;
    }

    public Calendar getUlTime() {
        return ulTime;
    }

    public boolean isDue() {
        return isDue;
    }

    public boolean isLock() {
        return isLock;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String getDetail() {
        return detail;
    }


}
