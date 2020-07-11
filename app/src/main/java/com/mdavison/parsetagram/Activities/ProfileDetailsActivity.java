package com.mdavison.parsetagram.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.mdavison.parsetagram.Fragments.ProfileFragment;
import com.mdavison.parsetagram.R;

/**
 * This activity hosts a fragment showing a profile's details
 */
public class ProfileDetailsActivity extends AppCompatActivity {
    public static final String TAG = "ProfileDetailsActivity";
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        // Quicker and Dirtier than QDOS... TODO: make an actual activity
        fragmentManager.beginTransaction()
                .replace(R.id.flContainer, new ProfileFragment()).commit();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
}