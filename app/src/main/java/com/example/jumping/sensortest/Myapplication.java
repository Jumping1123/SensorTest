package com.example.jumping.sensortest;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jumping on 2016/9/6.
 */
public class Myapplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
