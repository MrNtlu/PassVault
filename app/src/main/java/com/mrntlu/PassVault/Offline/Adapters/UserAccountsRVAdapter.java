package com.mrntlu.PassVault.Offline.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.mrntlu.PassVault.Offline.ClassController;
import com.mrntlu.PassVault.Offline.FileLocations;
import com.mrntlu.PassVault.Offline.Models.AccountsObject;
import com.mrntlu.PassVault.R;

import java.io.FileOutputStream;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class UserAccountsRVAdapter extends RecyclerView.Adapter<UserAccountsRVAdapter.MyViewHolder> {

    Context context;
    Dialog customDialog;

    FileOutputStream fos=null;
    FileOutputStream fosPass=null;

    private RealmResults<AccountsObject> userObjects;
    private ArrayList<Boolean> passBool;
    private boolean isSearching=false;

    Realm realm;
    ClassController classController;
    ArrayList<ArrayList> arrayLists;

    public UserAccountsRVAdapter(Context context, RealmResults<AccountsObject> userObjects, final ArrayList<Boolean> passBool) {
        this.context = context;
        this.userObjects=userObjects;
        this.passBool = passBool;
        classController=new ClassController(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_useraccounts_cell,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        realm=Realm.getDefaultInstance();
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.idText.setText(userObjects.get(position).getIdMail());
        holder.descriptionText.setText(userObjects.get(position).getDescription());

        if (isSearching) {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        if (userObjects.size()!=passBool.size() && passBool.size()<userObjects.size()) {
            passBool.add(false);
        }

        classController.passwordInitHider(passBool,holder.passwordText,userObjects.get(position).getPassword(),position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                classController.copyToClipboard(userObjects.get(position).getPassword());
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classController.passwordHider(passBool,holder.passwordText,userObjects.get(position).getPassword(),position);
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog=new Dialog(context);
                showPopup(v,position);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are You Sure?");
                builder.setMessage("Do you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                try{
                                    passBool.remove(position);
                                    userObjects.get(position).deleteFromRealm();
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,getItemCount());

                    }
                });
                builder.setNegativeButton("NO!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userObjects.size();
    }

    public void showPopup(View v,final int position){
        Button editAdd,editClose;
        final TextView editID,editPassword,editDesc;

        customDialog.setContentView(R.layout.dialog_useraccounts);

        editID=(TextView)customDialog.findViewById(R.id.editID);
        editPassword=(TextView)customDialog.findViewById(R.id.editpassword);
        editAdd=(Button)customDialog.findViewById(R.id.editAdd);
        editClose=(Button)customDialog.findViewById(R.id.editClose);
        editDesc=(TextView)customDialog.findViewById(R.id.editDes);

        editID.setText(userObjects.get(position).getIdMail());
        editPassword.setText(userObjects.get(position).getPassword());
        editDesc.setText(userObjects.get(position).getDescription());

        editAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!classController.isEmptyTextViews(new TextView[]{editID,editPassword,editDesc})){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            String id=editID.getText().toString();
                            String password=editPassword.getText().toString();
                            String desc=editDesc.getText().toString();
                            userObjects.get(position).setIdMail(id);
                            userObjects.get(position).setPassword(password);
                            userObjects.get(position).setDescription(desc);
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
        TextView descriptionText;
        TextView passwordText;
        ImageButton deleteButton;
        ImageButton editButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            idText=(TextView)itemView.findViewById(R.id.idText);
            descriptionText=(TextView)itemView.findViewById(R.id.descriptionText);
            passwordText=(TextView)itemView.findViewById(R.id.passwordText);
            deleteButton=(ImageButton) itemView.findViewById(R.id.deleteButton);
            editButton=(ImageButton) itemView.findViewById(R.id.editButton);
        }
    }
}
