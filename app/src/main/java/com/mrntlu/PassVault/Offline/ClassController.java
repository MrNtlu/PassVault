package com.mrntlu.PassVault.Offline;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mrntlu.PassVault.MainActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;

public class ClassController {

    Context context;
    String passHolder = "••••••••••";

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

    public void setColorImage(String text, ImageView img){
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();
        if (text!=null && img!=null) {
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(text.trim().substring(0, 1), color);

            img.setImageDrawable(drawable);
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
            if (position<passBool.size()) {
                if (!passBool.get(position)) {
                    passwordText.setText(passString);
                    passBool.set(position, true);
                } else if (passBool.get(position)) {
                    passwordText.setText(passHolder);
                    passBool.set(position, false);
                }
            }
        }catch (IndexOutOfBoundsException e){
            indexError(e);
        }
    }

    public void passwordInitHider(ArrayList<Boolean> passBool, TextView passwordText, String passString,int position){
        try {
            if (position<passBool.size()) {
                if (!passBool.get(position)) {
                    passwordText.setText(passHolder);
                } else {
                    passwordText.setText(passString);
                }
            }
        }catch (IndexOutOfBoundsException e){
            indexError(e);
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

    private void deleteAllCredentials(String FILE_NAME){
        File dir=context.getFilesDir();
        File file=new File(dir,FILE_NAME);
        boolean deleted=file.delete();
    }

    public void loadCredentials(String FILE_NAME,ArrayList<String> list){
        FileInputStream fis=null;
        try {
            File file=context.getFileStreamPath(FILE_NAME);
            if (file.exists()) {
                fis = context.openFileInput(FILE_NAME);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    list.add(text);
                }
                deleteAllCredentials(FILE_NAME);
            }
            else {
                return;
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
}
