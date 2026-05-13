package com.example.erasmusbuddy;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views for Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set Toolbar as Action Bar
        setSupportActionBar(toolbar);

        // Configure "hamburger" button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load Home Fragment by default
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment(), "Home");
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Handle sidebar menu clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    replaceFragment(new HomeFragment(), "Home");
                } else if (id == R.id.nav_challenges) {
                    replaceFragment(new ChallengesFragment(), "Survival Tasks");
                } else if (id == R.id.nav_emergency) {
                    replaceFragment(new EmergencyFragment(), "Emergency");
                } else if (id == R.id.nav_guide) {
                    replaceFragment(new CityGuideFragment(), "City Guide");
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * Replaces the current fragment in the container.
     * @param fragment The new fragment to show.
     * @param title The title for the Toolbar.
     */
    public void replaceFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        // Update the Toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
