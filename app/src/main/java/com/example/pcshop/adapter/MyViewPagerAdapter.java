package com.example.pcshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pcshop.fragments.CpuFragment;
import com.example.pcshop.fragments.GpuFragment;
import com.example.pcshop.fragments.MboFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CpuFragment();
            case 1:
                return new GpuFragment();
            case 2:
                return new MboFragment();
            default:
                return new CpuFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
