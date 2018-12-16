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
import com.mrntlu.PassVault.Offline.Adapters.MailVaultRVAdapter;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Set;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MailVault extends AppCompatActivity {

    TextView idInput,passInput;
    Button addButton,closeButton;
    ImageButton addMenuButton;
    RecyclerView recyclerView;
    ConstraintLayout addMenuLayout;
    ClassController classController;
    SearchView searchView;

    private ArrayList<String> idList=new ArrayList<String>();
    private ArrayList<String> passwordList=new ArrayList<String>();
    private ArrayList<Boolean> passBool=new ArrayList<Boolean>();

    MailVaultRVAdapter mailVaultRVAdapter;
    FileOutputStream fos=null;
    FileOutputStream fosPass=null;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MailVault.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_vault);
        addMenuLayout=(ConstraintLayout)findViewById(R.id.addMenuConstraint);
        idInput=(TextView)findViewById(R.id.idInput);
        passInput=(TextView)findViewById(R.id.passwordInput);
        addButton=(Button)findViewById(R.id.button);
        closeButton=(Button)findViewById(R.id.closeButton);
        addMenuButton=(ImageButton)findViewById(R.id.addButton);
        recyclerView =(RecyclerView)findViewById(R.id.listView);
        searchView=(SearchView) findViewById(R.id.mailSearchView);

        classController=new ClassController(this);
        classController.loadCredentials(FileLocations.FILE2_NAME,idList);
        classController.loadCredentials(FileLocations.PASS2_FILE_NAME,passwordList);

        //mailVaultRVAdapter=new MailVaultRVAdapter(this,idList,passwordList,passBool);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mailVaultRVAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] textViews=new TextView[]{idInput,passInput};
                ArrayList[] arrayLists=new ArrayList[]{idList,passwordList};

                if (!classController.isEmptyTextViews(textViews)){

                    classController.addNewItem(textViews,arrayLists,fosPass,
                            new String[]{FileLocations.FILE2_NAME,FileLocations.PASS2_FILE_NAME});
                    mailVaultRVAdapter.notifyDataSetChanged();
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
                    reSetAdapter(idList,passwordList,passBool,false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Set<Integer> indexs=classController.searchInArrayList(idList,s);
                ArrayList<String> tempIdList=new ArrayList<String>();
                ArrayList<String> tempPassList=new ArrayList<String>();
                for (int i:indexs){
                    tempIdList.add(idList.get(i));
                    tempPassList.add(passwordList.get(i));
                }
                reSetAdapter(tempIdList,tempPassList,new ArrayList<Boolean>(),true);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.trim().length()==0){
                    reSetAdapter(idList,passwordList,passBool,false);
                }
                return false;
            }
        });

        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

//                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Item deleted. Click UNDO to revert change.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        addItemToRealm(todoItem, position);
//                        recyclerViewAdapter.notifyItemInserted(position);
//                    }
//                });
//                snackbar.show();
//
//                Snackbar.make(findViewById(R.id.coordinatorLayout),"Cannot be deleted. Please check the box.",Snackbar.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);*/
    }

    private void reSetAdapter(ArrayList<String> idList, ArrayList<String> passwordList, ArrayList<Boolean> passBool,boolean isSearching){
        //mailVaultRVAdapter=new MailVaultRVAdapter(MailVault.this,idList,passwordList,passBool,isSearching);
        //recyclerView.setAdapter(mailVaultRVAdapter);
        mailVaultRVAdapter.notifyDataSetChanged();
    }
}
