package com.example.erasmusbuddy;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

/*
 * This is the Main Activity. It is like the "frame" of our app.
 * It holds the top bar (Toolbar) and the side menu (Navigation Drawer).
 */
public class MainActivity extends AppCompatActivity {

    // Variable for the side menu structure
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Find the top bar and set it up
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 2. Find the menu drawer and the list of items
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        // 3. Setup the "hamburger" button to open/close the menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // 4. When we first open the app, show the Home Fragment automatically
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            navView.setCheckedItem(R.id.nav_home);
        }

        // 5. Tell the app what to do when we click a menu item
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // Logic: if user clicks Home, show HomeFragment, etc.
                if (id == R.id.nav_home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                } else if (id == R.id.nav_challenges) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ChallengesFragment())
                            .commit();
                } else if (id == R.id.nav_emergency) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new EmergencyFragment())
                            .commit();
                } else if (id == R.id.nav_guide) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new CityGuideFragment())
                            .commit();
                }

                // Close the menu after we make a choice
                drawer.closeDrawers();
                return true;
            }
        });
    }
}
