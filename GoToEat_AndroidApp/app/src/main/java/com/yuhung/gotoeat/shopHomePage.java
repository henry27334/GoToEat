package com.yuhung.gotoeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.yuhung.gotoeat.FragmentModel.homeFragmentModel;
import com.yuhung.gotoeat.FragmentModel.memberFragmentModel;
import com.yuhung.gotoeat.FragmentModel.menuFragmentModel;
import com.yuhung.gotoeat.FragmentModel.orderFragmentModel;

import java.util.ArrayList;

public class shopHomePage extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView bottomNavigation;
    ViewPager2 viewPager;
    ViewPagerAdapter adapter;
    ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shophomepage);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        BindingID();

//        toolbar.setVisibility(View.INVISIBLE);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new homeFragmentModel());
        fragmentList.add(new menuFragmentModel());
        fragmentList.add(new orderFragmentModel());
        fragmentList.add(new memberFragmentModel());

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragmentList);
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(adapter);
        bottomNavigation.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED); //正常顯示導覽標籤
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.navigation_menu:
                        viewPager.setCurrentItem(1,false);
                        break;
                    case R.id.navigation_order:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.navigation_member:
                        viewPager.setCurrentItem(3, false);
                        break;
                }
                return true;
            }
        });

        Intent intent = getIntent();
        int selectedPage = intent.getIntExtra("selectedPage", 0);

        switch(selectedPage){
            case 3:
                bottomNavigation.setSelectedItemId(R.id.navigation_order);
                break;
            case 4:
                bottomNavigation.setSelectedItemId(R.id.navigation_member);
                break;
        }

    }//onCreate

    private void BindingID() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        viewPager = findViewById(R.id.viewPager);

    }

    public class ViewPagerAdapter extends FragmentStateAdapter {

        ArrayList<Fragment> arrFragment;

        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager,
                                @NonNull Lifecycle lifecycle,
                                ArrayList<Fragment> arrFragment) {
            super(fragmentManager, lifecycle);
            this.arrFragment = arrFragment;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return arrFragment.get(position);
        }

        @Override
        public int getItemCount() {
            return arrFragment.size();
        }


    }//end of adapter
}