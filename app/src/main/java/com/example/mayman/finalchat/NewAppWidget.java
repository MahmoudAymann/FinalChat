package com.example.mayman.finalchat;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,int appWidgetId)
    {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        String status= PreferenceManager.getDefaultSharedPreferences(context).getString("eTa0", "null");
        String name= PreferenceManager.getDefaultSharedPreferences(context).getString("eTa1", "null");
        String img= PreferenceManager.getDefaultSharedPreferences(context).getString("eTa2", "null");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.name, name);
        views.setTextViewText(R.id.status, status);
        if(img.length()>10)
        Picasso.with(context).load(img).centerCrop().resize(100,100).into(views,R.id.image, new int[] {appWidgetId});

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);



    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

