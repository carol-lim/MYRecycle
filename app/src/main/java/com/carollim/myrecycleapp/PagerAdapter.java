package com.carollim.myrecycleapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {
    private String[] titles = new String[]{};
    private String userType;


    public PagerAdapter(@NonNull HomeFragment fragmentActivity,String[] titles, String userType){
        super(fragmentActivity);
        this.titles = titles;
        this.userType = userType;
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //citizen
        if (userType.equals("0")){
            switch (position) {
                case 0:
                    return new ReserveNowFragment();
                case 1:
                    return new MyReserveFragment();
            }
        }
        //collector
        else if (userType.equals("1")){
            switch (position) {
                case 0:
                    return new ReservationFragment();
                case 1:
                    return new ToCollectFragment();
            }
        }

        return null;

    }
}
