package com.mrntlu.PassVault.Offline;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrntlu.PassVault.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import androidx.constraintlayout.widget.ConstraintLayout;
import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class ClassController {

    Context context;

    public ClassController(Context context) {
        this.context = context;
    }

    public void copyToClipboard(String text){
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(text, text);
            clipboard.setPrimaryClip(clip);
            Toasty.success(context, "Password Copied!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toasty.error(context,"Error! Please try again",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void indexError(Exception e){
        e.printStackTrace();
        Toasty.error(context,"Error Occured! "+e.getMessage(), Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    public void passwordHider(ArrayList<Boolean> passBool, TextView passwordText, String passString,int position){
        try {
            if (!passBool.get(position)) {
                passwordText.setText(passString);
                passBool.set(position, true);
            } else if (passBool.get(position)) {
                String passHolder = "";
                for (int i = 0; i < passString.length(); i++) {
                    passHolder += "*";
                }
                passwordText.setText(passHolder);
                passBool.set(position, false);
            }
        }catch (IndexOutOfBoundsException e){
            indexError(e);
        }
    }

    public void passwordInitHider(ArrayList<Boolean> passBool, TextView passwordText, String passString,int position){
        try {
            String passHolder = "";
            if (!passBool.get(position)) {
                for (int i = 0; i < passString.length(); i++) {
                    passHolder += "*";
                }
                passwordText.setText(passHolder);
            } else {
                passwordText.setText(passString);
            }
        }catch (IndexOutOfBoundsException e){
            indexError(e);
        }
    }

    public void deleteFromArrays(ArrayList<ArrayList> arrayLists, int position){
        for (int i=0;i<arrayLists.size();i++){
            arrayLists.get(i).remove(position);
        }
    }

    public boolean isEmptyTextViews(TextView[] textViews){
        for (int i=0;i<textViews.length;i++){
            if (textViews[i].getText().toString().trim().isEmpty()){
                Toasty.error(context, "Don't leave it empty!",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void saveCredentials(ArrayList<String> list, FileOutputStream fosPass, String FILE_NAME){
        String text="";
        for (int i=0;i<list.size();i++){
            text=text+list.get(i)+"\n";
        }
        try {
            fosPass=context.openFileOutput(FILE_NAME,MODE_PRIVATE);
            fosPass.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fosPass != null){
                try {
                    fosPass.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadCredentials(String FILE_NAME,ArrayList<String> list){
        FileInputStream fis=null;
        try {
            fis=context.openFileInput(FILE_NAME);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String text;
            while ((text=br.readLine())!=null){
                list.add(text);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteCredentials(String FILE_NAME,ArrayList<String> list,FileOutputStream fos){
        File dir = context.getFilesDir();
        File file = new File(dir,FILE_NAME);
        boolean deleted = file.delete();
        saveCredentials(list,fos,FILE_NAME);
    }

    public void addNewItem(TextView[] textViews,ArrayList[] arrayLists,FileOutputStream fos,String[] FILE_NAMES){
        for(int i=0;i<textViews.length;i++) {
            arrayLists[i].add(textViews[i].getText().toString());
            textViews[i].setText("");
        }
        for (int i=0;i<arrayLists.length;i++){
            saveCredentials(arrayLists[i],fos,FILE_NAMES[i]);
        }
    }

    public Set<Integer> searchInArrayList(ArrayList<String> array,String query){
        Set<Integer> treeSet = new TreeSet<>();
        for (String item:array){
            if (item.toLowerCase().contains(query.toLowerCase())){
                treeSet.add(array.indexOf(item));
            }
        }
        return treeSet;
    }

    public void closeBtnVisib(int visibility, SearchView searchView, ConstraintLayout constraintLayout, ImageButton imageBtn){
        if (visibility==0){
            constraintLayout.setAlpha((float)0);
            constraintLayout.animate().alpha(1).setDuration(320);

            constraintLayout.setVisibility(View.VISIBLE);
            imageBtn.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
        }else if (visibility==1){
            imageBtn.setAlpha((float)0);
            searchView.setAlpha((float)0);
            imageBtn.animate().alpha(1).setDuration(310);
            searchView.animate().alpha(1).setDuration(310);

            constraintLayout.setVisibility(View.GONE);
            imageBtn.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
        }
    }
}
