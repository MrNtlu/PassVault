package com.mrntlu.PassVault.Offline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import io.realm.Realm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mrntlu.PassVault.R;

public class FragmentOffline extends Fragment {

    private Realm realm;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private FragmentTransaction fragmentTransaction;

    public static FragmentOffline newInstance(){
        return new FragmentOffline();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_offline, container, false);
        viewPager=v.findViewById(R.id.view_pager);
        tabLayout=v.findViewById(R.id.tabLayout);
        fab=v.findViewById(R.id.addButton);
        realm=Realm.getDefaultInstance();
        OfflineViewPagerAdapter adapter=new OfflineViewPagerAdapter(getChildFragmentManager(),realm);
        viewPager.setAdapter(adapter);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentTransaction=((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
        tabLayout.setupWithViewPager(viewPager);
        fab.setOnClickListener(view1 -> {
            switch (tabLayout.getSelectedTabPosition()){
                case 0:
                case 1:
                case 2:
                    startTransaction(FragmentOfflineAdd.newInstance(tabLayout.getSelectedTabPosition()));
                    break;
            }
        });
    }

    private void startTransaction(Fragment fragment){
        fragmentTransaction.replace(R.id.frameLayout,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (realm!=null && realm.isClosed()){
            realm=Realm.getDefaultInstance();
        }else if (realm==null)
            realm=Realm.getDefaultInstance();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (realm!=null && !realm.isClosed()) {
            realm.close();
        }
    }
}
