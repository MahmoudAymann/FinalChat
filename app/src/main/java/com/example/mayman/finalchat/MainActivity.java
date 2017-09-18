package com.example.mayman.finalchat;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mayman.finalchat.services.MyJobService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Tabs
    ViewPager viewPager;
    SectionsPageAdapter sectionsPageAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MyJobService.class)
                // uniquely identifies the job
                .setTag("my-unique-tag")
                // one-off job
                .setRecurring(false)
                // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 5))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK
                ).build();
        int result = dispatcher.schedule(myJob);

        if (result != FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS) {
            Log.v("JOB_TAG", "ERROR ON SCHEDULE");}
        //-----------------------------------------------------
        //Tabs
        viewPager = (ViewPager) findViewById(R.id.pager_id);
        tabLayout = (TabLayout) findViewById(R.id.tab_main_layout);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPageAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }//end onCreate

    @Override
    public void onStart() {
        super.onStart();
        //Chck if user signin or not
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            updateCurrentUser();
        }
    }//end onStart

    private void updateCurrentUser() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }//end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int itemId = item.getItemId();
        if (itemId == R.id.menu_logout) {
            logout();
            updateCurrentUser();
        } else if (itemId == R.id.menu_allUser) {
            getAllUsers();
        } else if (itemId == R.id.menu_settingsID) {
            settings();
        }
        return true;
    }//end onOptionsItemSelected

    private void settings() {
         Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
    }

    private void getAllUsers() {
        Intent intent = new Intent(MainActivity.this,AllUsersActivity.class);
        startActivity(intent);
    }

    private void logout()
    {
        FirebaseAuth.getInstance().signOut();
    }

void MUKO()
{
    PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.et0),getString(R.string.NOP)).apply();
    PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.et1),getString(R.string.NOP2)).apply();
    PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.et2),getString(R.string.NOP3)).apply();

    UpdateWedgie();
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
}//end class