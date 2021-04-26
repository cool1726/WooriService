package com.example.wooriservice;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.wooriservice.calendars.Calendars;
import com.example.wooriservice.myacc.Myacc;
import com.example.wooriservice.report.reports;

import com.example.wooriservice.calendars.Calendars;
import com.example.wooriservice.myacc.Myacc;

import java.util.ArrayList;

public class ViewPagerA extends FragmentPagerAdapter {

    private ArrayList<Fragment> fList;

    public ViewPagerA(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        if(position == 0){
            fragment = new Myacc();
        }else if(position == 1){
            fragment = new Calendars();
        }else{
            fragment = new reports();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
