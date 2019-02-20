package com.kris.questademo2;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerData{
    private final static String settingName = "PlayerDatabase";
    private static Context mContext;
    private static SharedPreferences mSettings;
    private static SharedPreferences.Editor mEditor;
    private static final AtomicInteger IDs = new AtomicInteger(100); //Give Unique IDs to each Intent

    GsonBuilder gsonb = new GsonBuilder();
    Gson mGson = gsonb.create();

    public PlayerData(Context mContext){
        this.mContext = mContext;
        mSettings =  mContext.getSharedPreferences(settingName, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    public boolean writeJSON(Player player)
    {
        try {
            String writeValue = mGson.toJson(player);
            mEditor.putString(settingName, writeValue);
            mEditor.commit();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public Player readJSON()
    {
        String loadValue = mSettings.getString(settingName, "");
        Player player = mGson.fromJson(loadValue, Player.class);
        return player;
    }

    public static int getID() {
        int ID = IDs.getAndIncrement();
        return ID;
    }

}