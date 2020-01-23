package com.mrntlu.PassVault.Offline;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrntlu.PassVault.Common.BaseAdapter;
import com.mrntlu.PassVault.Offline.Adapters.OthersRVAdapter;
import com.mrntlu.PassVault.Offline.Models.OthersObject;
import com.mrntlu.PassVault.Offline.Viewmodels.OfflineViewModel;
import com.mrntlu.PassVault.R;

import java.util.ArrayList;

public class FragmentOtherAccounts extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private OthersRVAdapter othersRVAdapter;
    private ArrayList<Boolean> passBool=new ArrayList<>();
    private OfflineViewModel mViewModel;
    private FloatingActionButton fab;
    private Realm realm;
    private RealmResults<OthersObject> otherObjects;

    public static FragmentOtherAccounts newInstance(Realm realm) {
        FragmentOtherAccounts fragment = new FragmentOtherAccounts();
        fragment.realm=realm;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_other_accounts, container, false);
        recyclerView= v.findViewById(R.id.otherRV);
        searchView= v.findViewById(R.id.otherSearch);
        if (realm==null || realm.isClosed())
            realm=Realm.getDefaultInstance();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        initRecyclerView();

        mViewModel=ViewModelProviders.of(this).get(OfflineViewModel.class);
        mViewModel.initOtherObjects(realm);
        mViewModel.getmOtherObjects().observe(getViewLifecycleOwner(), othersObjects -> {
            this.otherObjects=othersObjects;
            if (othersRVAdapter==null){
                initRecyclerView();
            }else{
                othersRVAdapter.submitList(convertRealmListToList(othersObjects));
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (realm!=null) {
                    initRecyclerView();
                    RealmResults<OthersObject> searchOthers = mViewModel.searchOtherObject(s).getValue();
                    othersRVAdapter.submitList(convertRealmListToList(searchOthers));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length()==0){
                    othersRVAdapter.submitList(convertRealmListToList(otherObjects));
                }
                return false;
            }
        });

        fab=getActivity().findViewById(R.id.addButton);
        if (fab.getTranslationY()!=0.0){
            fab.setTranslationY(0f);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (fab != null) {
                    if (dy >= 1)
                        fab.hide();
                    else
                        fab.show();
                }
            }
        });

        return v;
    }

    private <E> ArrayList<E> convertRealmListToList(RealmResults<E> realmList){
        if (realmList!=null)
            return new ArrayList<>(realmList);
        else
            return new ArrayList<>();
    }

    private void initRecyclerView(){
        if (realm!=null) {
            othersRVAdapter = new OthersRVAdapter(getContext(), passBool, realm, getActivity(), position -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are You Sure?");
                builder.setMessage("Do you want to delete?");
                builder.setPositiveButton("Yes", (dialog, which) -> mViewModel.deleteOtherObject(otherObjects.get(position)));
                builder.setNegativeButton("NO!", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog=builder.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.white));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.white));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
            });
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
