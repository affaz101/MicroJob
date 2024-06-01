package com.abu.microjob.Controller;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.abu.microjob.Fragments.CandidateAuthFragment;
import com.abu.microjob.Fragments.RecriterAuthFragment;


public class AccountViewPagerAdapter extends FragmentStateAdapter {

    Fragment fragment;
    public AccountViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            fragment = new RecriterAuthFragment();
        } else if (position == 1) {
            fragment = new CandidateAuthFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
