package com.mrntlu.PassVault.Offline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import es.dmoral.toasty.Toasty;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.mrntlu.PassVault.Offline.Viewmodels.OfflineViewModel;
import com.mrntlu.PassVault.R;

public class OfflineAddActivity extends AppCompatActivity {

    Toolbar toolbar;
    int objectID=-1;
    EditText editText,editText2,editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_add);

        editText=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);
        editText3=findViewById(R.id.editText3);

        objectID=getIntent().getIntExtra("TAB_POSITION",-1);
        if (objectID==-1){
            Toasty.error(OfflineAddActivity.this,"Error Occured, Try again.",Toast.LENGTH_SHORT).show();
            finish();
        }
        if (objectID==1){
            editText3.setVisibility(View.VISIBLE);
        }else if (objectID==2){
            editText.setHint("Description");
        }

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(1);
        getSupportActionBar().setTitle("Save");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.addMenuButton:
                addRealmObject();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addRealmObject(){
        if (FragmentMailVault.staticViewModel!=null) {
            if (!(editText.toString().trim().equals("") && editText2.toString().trim().equals(""))) {
                switch (objectID) {
                    case 0:
                        FragmentMailVault.staticViewModel.addMailObject(editText.getText().toString(), editText2.getText().toString());
                        break;
                    case 1:
                        if (!editText3.toString().trim().equals("")) {
                            FragmentMailVault.staticViewModel.addUserObject(editText.getText().toString(), editText2.getText().toString(), editText3.getText().toString());
                        }else{

                        }
                        break;
                    case 2:
                        FragmentMailVault.staticViewModel.addOtherObject(editText.getText().toString(), editText2.getText().toString());
                        break;
                }
            }else{

            }
        }
        finish();
    }
}
