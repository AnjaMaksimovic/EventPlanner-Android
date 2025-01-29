package com.example.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.eventplanner.model.enums.Role;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityHomeBinding;
import com.example.eventplanner.settings.EmailVerificationSender;
import com.example.eventplanner.settings.PreferencesManager;
import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Set<Integer> topLevelDestinations = new HashSet<>();
    private Role userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "HomeActivity onCreate()");


        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toolbar = binding.activityHomeBase.toolbar;

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            actionBar.setHomeButtonEnabled(false);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(
               this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // This method will be called when the drawer is sliding
                userRole = PreferencesManager.getLoggedUserRole(HomeActivity.this);
                Log.d("HomeActivity", "UserRole on drawer open: " + userRole);

                MenuItem pricelistItem = binding.navView.getMenu().findItem(R.id.nav_pricelist);
                if (userRole != null) {
                    if (userRole.equals(Role.owner) || userRole.equals(Role.employee)) {
                        pricelistItem.setVisible(true);
                    } else {
                        pricelistItem.setVisible(false);
                    }
                }

                MenuItem companiesListITem = binding.navView.getMenu().findItem(R.id.nav_companies_list);
                if (userRole != null) {
                    if (userRole.equals(Role.organizator) ) {
                        companiesListITem.setVisible(true);
                    } else {
                        companiesListITem.setVisible(false);
                    }
                }
                MenuItem userReportsItem = binding.navView.getMenu().findItem(R.id.nav_user_reports);
                if (userRole != null) {
                    if (userRole.equals(Role.admin) ) {
                        userReportsItem.setVisible(true);
                    } else {
                        userReportsItem.setVisible(false);
                    }
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // This method will be called when the drawer is fully opened

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // This method will be called when the drawer is fully closed
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // This method will be called when the drawer's state changes
            }
        });


        topLevelDestinations.add(R.id.nav_login);

        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            Log.i("ShopApp", "Destination Changed");
            int id = navDestination.getId();
            boolean isTopLevelDestination = topLevelDestinations.contains(id);
            /* Logic to determine if the destination is top level */;

            if (!isTopLevelDestination) {
                switch (id) {

                }
                // Close the drawer if the destination is not a top level destination
                drawer.closeDrawers();
            }else{
                if (id == R.id.nav_login) {
                    Toast.makeText(HomeActivity.this, "Login", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mAppBarConfiguration = new AppBarConfiguration
                .Builder(R.id.nav_products)
                .setOpenableLayout(drawer)
                .build();
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        //EmailVerificationSender.SendVerificationEmails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu.clear();
        // koristimo ako je nasa arhitekrura takva da imamo jednu aktivnost
        // i vise fragmentaa gde svaki od njih ima svoj menu unutar toolbar-a

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.nav_settings:
//                Toast.makeText(HomeActivity.this, "Settings", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_language:
//                Toast.makeText(HomeActivity.this, "Language", Toast.LENGTH_SHORT).show();
//                break;
//        }
        // U ovoj metodi, prvo se pomoću Navigation komponente pronalazi NavController.
        // NavController je odgovoran za upravljanje navigacijom unutar aplikacije
        // koristeći Androidov servis za navigaciju.
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);

        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

}