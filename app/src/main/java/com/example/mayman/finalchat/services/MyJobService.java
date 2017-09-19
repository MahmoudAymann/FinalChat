package com.example.mayman.finalchat.services;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import com.example.mayman.finalchat.NewAppWidget;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
/**
 * Created by Ahmed on 9/19/2017.
 */

public class MyJobService extends JobService
{

    @Override
    public boolean onStartJob(JobParameters job)
    {
        Log.v("mnm","jop started");
        UpdateWedgie();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job)
    {
        Log.v("mnm","interupted");
        return false;
    }
    void UpdateWedgie()
    {
        Log.v("mnm","On Update");
        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] ai = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ai);
        sendBroadcast(intent);
    }
}
