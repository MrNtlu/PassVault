package com.mrntlu.PassVault.Online;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrntlu.PassVault.R;

public class FragmentPasswordGenerator extends Fragment {

    public FragmentPasswordGenerator() {
        // Required empty public constructor
    }

    public static FragmentPasswordGenerator newInstance() {
        FragmentPasswordGenerator fragment = new FragmentPasswordGenerator();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_generator, container, false);
    }
}
