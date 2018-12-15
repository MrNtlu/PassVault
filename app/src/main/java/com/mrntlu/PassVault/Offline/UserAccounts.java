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
import com.mrntlu.PassVault.Adapters.UserAccountsRVAdapter;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Set;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserAccounts extends AppCompatActivity {

    TextView idInput,passInput,descInput;
    Button addButton,closeButton;
    ImageButton addMenuButton;
    RecyclerView recyclerView;
    ClassController classController;
    SearchView searchView;
    ConstraintLayout addMenuLayout;

    private ArrayList<String> idList=new ArrayList<String>();
    private ArrayList<String> passwordList=new ArrayList<String>();
    private ArrayList<String> descList=new ArrayList<String>();
    private ArrayList<Boolean> passBool=new ArrayList<Boolean>();

    UserAccountsRVAdapter userAccountsRVAdapter;
    FileOutputStream fos=null;
    FileOutputStream fosPass=null;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(UserAccounts.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_accounts);
        idInput=(TextView)findViewById(R.id.idInput);
        passInput=(TextView)findViewById(R.id.passwordInput);
        addButton=(Button)findViewById(R.id.button);
        recyclerView =(RecyclerView)findViewById(R.id.listView);
        descInput=(TextView)findViewById(R.id.descInput);
        closeButton=(Button)findViewById(R.id.closeButton);
        addMenuButton=(ImageButton)findViewById(R.id.addButton);
        searchView=(SearchView) findViewById(R.id.userAccountSearchView);
        addMenuLayout=(ConstraintLayout)findViewById(R.id.addMenuConstraint);

        classController=new ClassController(this);
        classController.loadCredentials(FileLocations.FILE_NAME,idList);
        classController.loadCredentials(FileLocations.PASS_FILE_NAME,passwordList);
        classController.loadCredentials(FileLocations.DES_FILE_NAME,descList);
        userAccountsRVAdapter=new UserAccountsRVAdapter(this,idList,passwordList,descList,passBool);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAccountsRVAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] textViews=new TextView[]{idInput,passInput,descInput};
                ArrayList[] arrayLists=new ArrayList[]{idList,passwordList,descList};
                if (!classController.isEmptyTextViews(textViews)){

                    classController.addNewItem(textViews,arrayLists,
                            fosPass,new String[]{FileLocations.FILE_NAME,FileLocations.PASS_FILE_NAME,FileLocations.DES_FILE_NAME});
                    userAccountsRVAdapter.notifyDataSetChanged();
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
                    reSetAdapter(idList,passwordList,descList,passBool,false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Set<Integer> indexs=classController.searchInArrayList(descList,s);
                ArrayList<String> tempIdList=new ArrayList<String>();
                ArrayList<String> tempPassList=new ArrayList<String>();
                ArrayList<String> tempDescList=new ArrayList<String>();
                for (int i:indexs){
                    tempIdList.add(idList.get(i));
                    tempPassList.add(passwordList.get(i));
                    tempDescList.add(descList.get(i));
                }
                reSetAdapter(tempIdList,tempPassList,tempDescList,new ArrayList<Boolean>(),true);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length()==0){
                    reSetAdapter(idList,passwordList,descList,passBool,false);
                }
                return false;
            }
        });
    }

    private void reSetAdapter(ArrayList<String> idList,ArrayList<String> passwordList,ArrayList<String> descList,ArrayList<Boolean> passBool,boolean isSearching){
        userAccountsRVAdapter=new UserAccountsRVAdapter(UserAccounts.this,idList,passwordList,descList,passBool,isSearching);
        recyclerView.setAdapter(userAccountsRVAdapter);
        userAccountsRVAdapter.notifyDataSetChanged();
    }
}
