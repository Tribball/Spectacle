package com.mvye.spectacle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mvye.spectacle.fragments.HomeFragment;
import com.mvye.spectacle.fragments.ProfileFragment;
import com.mvye.spectacle.fragments.ShowSearchFragment;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationBar;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpVariables();
        setUpBottomNavigationBar();
    }

    private void setUpVariables() {
        bottomNavigationBar = findViewById(R.id.bottom_navigation);
    }

    private void setUpBottomNavigationBar() {
        bottomNavigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new HomeFragment();
                if (item.getItemId() == R.id.action_home) {
                    fragment = new HomeFragment();
                }
                else if (item.getItemId() == R.id.action_all_shows) {
                    fragment = new ShowSearchFragment();
                }
                else if (item.getItemId() == R.id.action_profile) {
                    fragment = new ProfileFragment();
                }
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).addToBackStack("").commit();
                return true;
            }
        });
        bottomNavigationBar.setSelectedItemId(R.id.action_home);
    }
}