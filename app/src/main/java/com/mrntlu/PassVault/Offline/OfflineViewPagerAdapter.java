package com.mrntlu.PassVault.Offline;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class OfflineViewPagerAdapter extends FragmentStatePagerAdapter {

    public OfflineViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment returnFragment;
        switch (position) {
            case 0:
                returnFragment=FragmentMailVault.newInstance();
                break;
            case 1:
                returnFragment=FragmentMailVault.newInstance();
                break;
            case 2:
                returnFragment=FragmentMailVault.newInstance();
                break;
            default:
                return FragmentMailVault.newInstance();
        }
        return returnFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title;
        switch (position){
            case 0:
                title="Mails";
                break;
            case 1:
                title="Accounts";
                break;
            case 2:
                title="Others";
                break;
            default:
                return "Mails";
        }
        return title;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
