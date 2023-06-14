package com.wraith.wiregrard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wraith.wiregrard.Adapter.TabAdapter;
import com.wraith.wiregrard.Fragments.FragmentFree;
import com.wraith.wiregrard.Fragments.FragmentPremium;

public class HomeActivity extends AppCompatActivity {
    ViewPager2 myViewPager2;
    TabAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbarold);
        toolbar.setTitle("Servers");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myViewPager2 = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), getLifecycle());

        adapter.addFragment(new FragmentFree(), "Free Servers");
        adapter.addFragment(new FragmentPremium(), "Premium Servers");


        myViewPager2.setAdapter(adapter);
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        new TabLayoutMediator(tabLayout, myViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String tabTitle = "";
                if (position == 0) {
                    tabTitle = "Free Server";
                    myViewPager2.setCurrentItem(0);
                } else if (position == 1) {
                    myViewPager2.setCurrentItem(1);
                    tabTitle = "Premium Server";
                }
                tab.setText(tabTitle);
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myViewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}