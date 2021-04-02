package com.nila.spareroomapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.nila.spareroomapp.R;
import com.nila.spareroomapp.model.UpcomingModel;
import com.nila.spareroomapp.viewmodel.ListUpcomingViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private Context context;
    public static int REQUEST_PERMISSION=1;


    //@BindView(R.id.toolbar)
    private Toolbar toolbar;

    @BindView(R.id.roomtablayout)
    public TabLayout tabLayout;

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    private RoomTabAdapter adapter;

    int callPermissionCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }

        setContentView(R.layout.activity_main);

        callPermissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);

        if (callPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    0);
        }

        toolbar = findViewById(R.id.toolbar);

        ButterKnife.bind(this);

        if(getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }else toolbar.setVisibility(View.GONE);
        getSupportActionBar().setTitle("SpeedRoommating");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setElevation(0);


        viewPager.setOffscreenPageLimit(3);
        //CreateTabFragment();

        adapter = new RoomTabAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new UpcomingFragment(), "UPCOMING");
        adapter.addFragment(new UpcomingFragment(), "ARCHIVED");
        adapter.addFragment(new UpcomingFragment(), "OPTIONS");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        highLightCurrentTab(0); // for initial selected tab view
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position); // for tab change
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }
    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setTitle("Roommating");
                alertDialogBuilder.setMessage("Please enable Telephone permission to make phone call")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }
}