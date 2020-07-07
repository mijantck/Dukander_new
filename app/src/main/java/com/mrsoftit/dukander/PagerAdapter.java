package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private String fragmnet[] = {"অফার ", "রিচার্জ "};

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                recharech_offer_list recharech_offer_list = new recharech_offer_list();
                return recharech_offer_list;
            case 1:
                recharech_buy_list recharech_buy_list = new recharech_buy_list();
                return recharech_buy_list;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return fragmnet.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmnet[position];
    }
}
