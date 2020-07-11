package com.mdavison.parsetagram.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mdavison.parsetagram.Fragments.ComposeFragment;
import com.mdavison.parsetagram.Fragments.PostsFragment;
import com.mdavison.parsetagram.Fragments.ProfileFragment;
import com.mdavison.parsetagram.R;

/**
 * Hosts the BottomNavigationView and most functions of the app within fragments
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem menuItem) {
                        Fragment fragment;
                        switch (menuItem.getItemId()) {
                            case R.id.action_home:
                                fragment = new PostsFragment();
                                break;
                            case R.id.action_compose:
                                fragment = new ComposeFragment();
                                break;
                            case R.id.action_profile:
                            default:
                                fragment = new ProfileFragment();
                                break;
                        }
                        fragmentManager.beginTransaction()
                                .replace(R.id.flContainer, fragment).commit();
                        return true;
                    }
                });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setItemIconTintList(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
}