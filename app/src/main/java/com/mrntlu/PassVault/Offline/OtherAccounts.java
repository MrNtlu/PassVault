package com.mrntlu.PassVault.Offline;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.mrntlu.PassVault.Offline.Adapters.OthersRVAdapter;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Set;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OtherAccounts extends AppCompatActivity {

    TextView otherDesc,otherPass;
    RecyclerView recyclerView;
    Button otherAddButton,closeButton;
    ClassController classController;
    SearchView searchView;
    ConstraintLayout addMenuLayout;
    ImageButton addMenuButton;

    private ArrayList<String> descList=new ArrayList<String>();
    private ArrayList<String> passList=new ArrayList<String>();
    private ArrayList<Boolean> passBool=new ArrayList<Boolean>();

    OthersRVAdapter othersRVAdapter;
    FileOutputStream fos=null;
    FileOutputStream fosPass=null;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(OtherAccounts.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_accounts);
        addMenuLayout=(ConstraintLayout)findViewById(R.id.addMenuConstraint);
        otherDesc=(TextView)findViewById(R.id.otherDesc);
        otherPass=(TextView)findViewById(R.id.otherPassword);
        recyclerView =(RecyclerView) findViewById(R.id.otherList);
        otherAddButton=(Button)findViewById(R.id.otherAddButton);
        closeButton=(Button)findViewById(R.id.closeButton);
        addMenuButton=(ImageButton)findViewById(R.id.addButton);
        searchView=(SearchView) findViewById(R.id.otherSearchView);

        classController=new ClassController(this);
        classController.loadCredentials(FileLocations.FILE3_NAME,descList);
        classController.loadCredentials(FileLocations.PASS3_FILE_NAME,passList);

        //othersRVAdapter=new OthersRVAdapter(this,descList,passList,passBool);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(othersRVAdapter);

        otherAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] textViews=new TextView[]{otherDesc,otherPass};
                ArrayList[] arrayLists=new ArrayList[]{descList,passList};

                if (!classController.isEmptyTextViews(textViews)){

                    classController.addNewItem(textViews,arrayLists,
                            fosPass,new String[]{FileLocations.FILE3_NAME,FileLocations.PASS3_FILE_NAME});
                    othersRVAdapter.notifyDataSetChanged();
                }
            }
        });

        addMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classController.closeBtnVisib(0,searchView,addMenuLayout,addMenuButton);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classController.closeBtnVisib(1,searchView,addMenuLayout,addMenuButton);

            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    addMenuButton.setVisibility(View.GONE);
                }
                else{
                    addMenuButton.setVisibility(View.VISIBLE);
                    searchView.setQuery("",false);
                    reSetAdapter(descList,passList,passBool,false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Set<Integer> indexs=classController.searchInArrayList(descList,s);
                ArrayList<String> tempIdList=new ArrayList<String>();
                ArrayList<String> tempPassList=new ArrayList<String>();
                for (int i:indexs){
                    tempIdList.add(descList.get(i));
                    tempPassList.add(passList.get(i));
                }
                reSetAdapter(tempIdList,tempPassList,new ArrayList<Boolean>(),true);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length()==0){
                    reSetAdapter(descList,passList,passBool,false);
                }
                return false;
            }
        });
    }

    private void reSetAdapter(ArrayList<String> descList,ArrayList<String> passList,ArrayList<Boolean> passBool,boolean isSearching){
        //othersRVAdapter=new OthersRVAdapter(OtherAccounts.this,descList,passList,passBool,isSearching);
        recyclerView.setAdapter(othersRVAdapter);
        othersRVAdapter.notifyDataSetChanged();
    }
}