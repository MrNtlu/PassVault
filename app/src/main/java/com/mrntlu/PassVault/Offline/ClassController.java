package com.mrntlu.PassVault.Offline;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mrntlu.PassVault.MainActivityOld;
import com.mrntlu.PassVault.Online.Viewmodels.OnlineViewModel;
import java.util.ArrayList;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmObject;

public class ClassController {

    private Context context;
    private String passHolder = "••••••••••";

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
        Intent intent=new Intent(context, MainActivityOld.class);
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
        for (TextView textView : textViews) {
            if (textView.getText().toString().trim().isEmpty()) {
                Toasty.error(context, "Don't leave it empty!",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void adapterMoveOnlineButton(FragmentActivity fragmentActivity,String title,String id,String password){
        if (fragmentActivity!=null) {
            OnlineViewModel.addOnlineObject(fragmentActivity, title, id, password);
        }
    }

    public void adapterDeleteButton(final Realm realm, final ArrayList<Boolean> passBool,final RealmObject object, final int position, final RecyclerView.Adapter adapter){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are You Sure?");
        builder.setMessage("Do you want to delete?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            realm.executeTransaction(realm1 -> {
                try {
                    passBool.remove(position);
                    object.deleteFromRealm();
                }catch (NullPointerException e){
                    e.printStackTrace();
                    if (e.getMessage()!=null)
                        Toasty.error(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
            adapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("NO!", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
