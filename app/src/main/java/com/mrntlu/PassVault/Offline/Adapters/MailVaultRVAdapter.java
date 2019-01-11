package com.mrntlu.PassVault.Offline.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.mrntlu.PassVault.Offline.ClassController;
import com.mrntlu.PassVault.Offline.Models.MailObject;
import com.mrntlu.PassVault.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class MailVaultRVAdapter extends RecyclerView.Adapter<MailVaultRVAdapter.MyViewHolder> {

    private Context context;
    private Dialog customDialog;

    private RealmResults<MailObject> mailObjects;
    private ArrayList<Boolean> passBool;
    private ClassController classController;
    private Realm realm;
    private FragmentActivity fragmentActivity;
    private MailVaultRVAdapter adapter;

    private boolean isSearching=false;

    public MailVaultRVAdapter(Context context, final RealmResults<MailObject> mailObjects, final ArrayList<Boolean> passBool, Realm realm, FragmentActivity fragmentActivity) {
        this.context = context;
        this.mailObjects=mailObjects;
        this.passBool=passBool;
        this.realm=realm;
        this.fragmentActivity=fragmentActivity;
        classController=new ClassController(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_mail_cell,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.idText.setText(mailObjects.get(position).getMail());
        classController.setColorImage(holder.idText.getText().toString(),holder.img);

        if (isSearching) {
            holder.menuButton.setVisibility(View.GONE);
        }

        if (mailObjects.size()!=passBool.size() && passBool.size()<mailObjects.size()) {
            passBool.add(false);
        }

        classController.passwordInitHider(passBool,holder.passwordText,mailObjects.get(position).getPassword(),position);
        
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                classController.copyToClipboard(mailObjects.get(position).getPassword());
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classController.passwordHider(passBool,holder.passwordText,mailObjects.get(position).getPassword(),position);
            }
        });


        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.offline_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.editMenuButton:
                                customDialog=new Dialog(context);
                                showPopup(position);
                                return true;
                            case R.id.deleteMenuButton:
                                classController.adapterDeleteButton(realm,passBool,mailObjects.get(position),position,adapter);
                                return true;
                            case R.id.moveMenuButton:
                                classController.adapterMoveOnlineButton(fragmentActivity,"Please Edit Title",mailObjects.get(position).getMail(),mailObjects.get(position).getPassword());
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        adapter=(MailVaultRVAdapter)recyclerView.getAdapter();
    }

    @Override
    public int getItemCount() {
        return mailObjects.size();
    }

    public void showPopup(final int position){
        Button editAdd,editClose;
        final TextView editID,editPassword;
        if (Build.VERSION.SDK_INT==21){
            customDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        customDialog.setContentView(R.layout.dialog_mail_other);

        editID=(TextView)customDialog.findViewById(R.id.editID);
        editPassword=(TextView)customDialog.findViewById(R.id.editpassword);
        editAdd=(Button)customDialog.findViewById(R.id.editAdd);
        editClose=(Button)customDialog.findViewById(R.id.editClose);

        editID.setText(mailObjects.get(position).getMail());
        editPassword.setText(mailObjects.get(position).getPassword());

        editAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!classController.isEmptyTextViews(new TextView[] {editID, editPassword})){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            String id=editID.getText().toString();
                            String password=editPassword.getText().toString();
                            mailObjects.get(position).setPassword(password);
                            mailObjects.get(position).setMail(id);
                        }
                    });
                    notifyDataSetChanged();
                    customDialog.dismiss();
                }
            }
        });

        editClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView idText;
        TextView passwordText;
        ImageButton menuButton;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            idText=(TextView)itemView.findViewById(R.id.idText);
            passwordText=(TextView)itemView.findViewById(R.id.passwordText);
            menuButton=(ImageButton) itemView.findViewById(R.id.menuButton);
            img=itemView.findViewById(R.id.imageColor);
        }
    }
}
