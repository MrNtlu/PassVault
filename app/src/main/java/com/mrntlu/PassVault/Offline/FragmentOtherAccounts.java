package com.mrntlu.PassVault.Offline;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrntlu.PassVault.Offline.Adapters.OthersRVAdapter;
import com.mrntlu.PassVault.Offline.Models.OthersObject;
import com.mrntlu.PassVault.Offline.Viewmodels.OfflineViewModel;
import com.mrntlu.PassVault.R;

import java.util.ArrayList;

public class FragmentOtherAccounts extends Fragment {

    private View v;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private OthersRVAdapter othersRVAdapter;
    private ArrayList<Boolean> passBool=new ArrayList<>();
    private OfflineViewModel mViewModel;
    private FloatingActionButton fab;
    private Realm realm;

    public static FragmentOtherAccounts newInstance(Realm realm) {
        FragmentOtherAccounts fragment = new FragmentOtherAccounts();
        fragment.realm=realm;
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
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        mViewModel=ViewModelProviders.of(getActivity()).get(OfflineViewModel.class);
        mViewModel.initOtherObjects(realm);
        mViewModel.getmOtherObjects().observe(getViewLifecycleOwner(), new Observer<RealmResults<OthersObject>>() {
            @Override
            public void onChanged(RealmResults<OthersObject> othersObjects) {
                if (othersRVAdapter==null){
                    initRecyclerView();
                }else{
                    othersRVAdapter.notifyDataSetChanged();
                }
            }
        });
        initRecyclerView();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    initRecyclerView();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (realm!=null) {
                    RealmResults<OthersObject> searchOthers = mViewModel.searchOtherObject(s).getValue();
                    othersRVAdapter = new OthersRVAdapter(getContext(), searchOthers, passBool, realm);
                    recyclerView.setAdapter(othersRVAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length()==0){
                    initRecyclerView();
                }
                return false;
            }
        });

        fab=(FloatingActionButton) getActivity().findViewById(R.id.addButton);
        if (fab.getTranslationY()!=0.0){
            fab.setTranslationY(0f);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (fab != null) {
                    if (dy >= 1) {
                        if (fab.getTranslationY()==0.0) {
                            fab.animate().translationYBy(500).setDuration(150);
                        }else{
                            fab.setTranslationY(500f);
                        }
                    } else {
                        if (fab.getTranslationY()==500.0) {
                            fab.animate().translationYBy(-500).setDuration(150);
                        }else{
                            fab.setTranslationY(0f);
                        }
                    }
                }
            }
        });

        return v;
    }

    private void initRecyclerView(){
        if (realm!=null) {
            othersRVAdapter = new OthersRVAdapter(getContext(), mViewModel.getmOtherObjects().getValue(), passBool,realm);
            recyclerView.setAdapter(othersRVAdapter);
        }else{
            realm=Realm.getDefaultInstance();
            initRecyclerView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fab!=null && fab.getTranslationY()!=0f){
            fab.setTranslationY(0f);
        }
    }
}
