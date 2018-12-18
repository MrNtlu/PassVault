package com.mrntlu.PassVault.Offline;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.mrntlu.PassVault.Offline.Adapters.OthersRVAdapter;
import com.mrntlu.PassVault.R;

import java.util.ArrayList;

public class FragmentOtherAccounts extends Fragment {

    private View v;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private OthersRVAdapter othersRVAdapter;
    private ArrayList<Boolean> passBool=new ArrayList<>();

    public static FragmentOtherAccounts newInstance() {
        FragmentOtherAccounts fragment = new FragmentOtherAccounts();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_other_accounts, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.otherRV);
        searchView=(SearchView)v.findViewById(R.id.otherSearch);
        return v;
    }

    private void initRecyclerView(){
        //othersRVAdapter=new OthersRVAdapter()getContext(),.getMailObjects().getValue(),passBool);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(othersRVAdapter);
    }

}
