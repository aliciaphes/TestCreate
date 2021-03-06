package com.example.testcreate;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EventsPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = {"Places", "Friends"};


    //how the adapter gets the manager to insert/remove fragments from activity
    public EventsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    //control order and creation of fragments within the pager
    @Override
    public Fragment getItem(int position) {
        //once created, they will be automatically cached
        if (position == 0) {
            return new PlacesFragment(); //PlacesFragment2 works PERFECTLY, DO NOT TOUCH
        } else if (position == 1) {
            return new FriendsFragment();
        } else {
            return null;
        }
    }

    //how many fragments there are to swipe between
    @Override
    public int getCount() {
        return tabTitles.length;
    }


    //return the tab title
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
