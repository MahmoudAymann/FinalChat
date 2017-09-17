package com.example.mayman.finalchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


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

    private void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}//end class