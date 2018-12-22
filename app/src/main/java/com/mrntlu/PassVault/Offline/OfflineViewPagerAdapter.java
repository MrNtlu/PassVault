package com.mrntlu.PassVault.Offline;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import io.realm.Realm;

public class OfflineViewPagerAdapter extends FragmentStatePagerAdapter {

    Realm realm;

    public OfflineViewPagerAdapter(FragmentManager fm, Realm realm) {
        super(fm);
        this.realm=realm;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment returnFragment;
        switch (position) {
            case 0:
                returnFragment=FragmentMailVault.newInstance(realm);
                break;
            case 1:
                returnFragment=FragmentUserAccounts.newInstance(realm);
                break;
            case 2:
                returnFragment=FragmentOtherAccounts.newInstance(realm);
                break;
            default:
                return FragmentMailVault.newInstance(realm);
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
