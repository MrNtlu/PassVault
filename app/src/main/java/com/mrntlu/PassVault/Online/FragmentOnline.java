package com.mrntlu.PassVault.Online;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.Online.Adapters.OnlineRVAdapter;
import com.mrntlu.PassVault.Online.Viewmodels.OnlineViewModel;
import com.mrntlu.PassVault.R;
import com.parse.ParseUser;


public class FragmentOnline extends Fragment {

    private BottomAppBar bottomAppBar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private BottomSheetDialog bottomSheetDialog;
    private OnlineDialog dialogClass;
    private OnlineViewModel viewModel;
    private OnlineRVAdapter onlineRVAdapter;

    public static FragmentOnline newInstance() {
        return new FragmentOnline();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_online, container, false);
        bottomAppBar=v.findViewById(R.id.bottomAppBar);
        recyclerView=v.findViewById(R.id.onlineRV);
        fab=v.findViewById(R.id.fab);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ParseUser user=ParseUser.getCurrentUser();
        if (user==null){
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
        viewModel= ViewModelProviders.of(this).get(OnlineViewModel.class);
        viewModel.initOnlineObjects();
        dialogClass=new OnlineDialog(viewModel.getOnlineObjects().getValue(),getContext(),viewModel);

        setRecyclerView();
        setListeners();
        setBottomSheetDialog(view);
        setupObserver();
    }

    private void setupObserver() {
        viewModel.getOnlineObjects().observe(getViewLifecycleOwner(), parseObjects -> {
            if (onlineRVAdapter == null) {
                setRecyclerView();
            } else {
                onlineRVAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setListeners() {
        fab.setOnClickListener(view -> {
            dialogClass.showAddDialog();
        });

        bottomAppBar.setNavigationOnClickListener(view -> bottomSheetDialog.show());
    }

    private void setBottomSheetDialog(View v){
        bottomSheetDialog=new BottomSheetDialog(v.getContext());
        View bottomSheetDialogView=getLayoutInflater().inflate(R.layout.dialog_bottom_sheet,null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);

        View logOut=bottomSheetDialogView.findViewById(R.id.logOut);

        logOut.setOnClickListener((view -> {
            ParseUser.logOutInBackground(e -> {
                if (e==null){
                    Toasty.info(view.getContext(),"Logged out.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }else{
                    if (e.getMessage()!=null)
                        Toasty.error(view.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
            bottomSheetDialog.dismiss();
        }));
    }

    private void setRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        onlineRVAdapter=new OnlineRVAdapter(viewModel.getOnlineObjects().getValue(),getContext(),viewModel);
        recyclerView.setAdapter(onlineRVAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).getAdView().setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getContext()!=null)
            ((MainActivity)getContext()).getAdView().setVisibility(View.VISIBLE);
    }

    /*FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment=null;
    switch (menuItem.getItemId()){
        case R.id.main_menu:
            startActivity(new Intent(FragmentOnline.this,MainActivity.class));
            finish();
            break;
        case R.id.online_storage:
            fragment=FragmentOnlineStorage.newInstance();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
            break;
        case R.id.pass_generator:
            fragment=FragmentPasswordGenerator.newInstance();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit();
            break;
        case R.id.log_out:
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        Toasty.info(FragmentOnline.this,"Logged out.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FragmentOnline.this,FragmentLogin.class));
                    }else{
                        Toasty.error(FragmentOnline.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }*/

}
