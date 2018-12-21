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
import com.mrntlu.PassVault.Offline.Models.OthersObject;
import com.mrntlu.PassVault.Offline.OtherAccounts;
import com.mrntlu.PassVault.R;

import java.io.FileOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class OthersRVAdapter extends RecyclerView.Adapter<OthersRVAdapter.MyViewHolder> {

    Context context;
    Dialog customDialog;

    FileOutputStream fos=null;
    FileOutputStream fosPass=null;

    private RealmResults<OthersObject> otherObjects;
    private ArrayList<Boolean> passBool;
    ClassController classController;
    ArrayList<ArrayList> arrayLists;
    Realm realm;

    private boolean isSearching=false;

    public OthersRVAdapter(Context context, final RealmResults<OthersObject> otherObjects, final ArrayList<Boolean> passBool) {
        this.context = context;
        this.otherObjects=otherObjects;
        this.passBool = passBool;
        classController=new ClassController(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_other_cell,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        realm=Realm.getDefaultInstance();
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.idText.setText(otherObjects.get(position).getDescription());

        if (isSearching) {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }

        if (otherObjects.size()!=passBool.size() && passBool.size()<otherObjects.size()) {
            passBool.add(false);
        }

        classController.passwordInitHider(passBool,holder.passwordText,otherObjects.get(position).getPassword(),position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                classController.copyToClipboard(otherObjects.get(position).getPassword());
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classController.passwordHider(passBool,holder.passwordText,otherObjects.get(position).getPassword(),position);
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
                                    otherObjects.get(position).deleteFromRealm();
                                }catch (NullPointerException e){
                                    Toasty.error(context,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
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
        return otherObjects.size();
    }

    public void showPopup(View v,final int position){
        Button editAdd,editClose;
        final TextView editDesc,editPassword;

        customDialog.setContentView(R.layout.dialog_mail_other);

        editDesc=(TextView)customDialog.findViewById(R.id.editID);
        editPassword=(TextView)customDialog.findViewById(R.id.editpassword);
        editAdd=(Button)customDialog.findViewById(R.id.editAdd);
        editClose=(Button)customDialog.findViewById(R.id.editClose);

        editDesc.setText(otherObjects.get(position).getDescription());
        editPassword.setText(otherObjects.get(position).getPassword());

        editAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!classController.isEmptyTextViews(new TextView[]{editDesc,editPassword})){
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            String desc=editDesc.getText().toString();
                            String password=editPassword.getText().toString();
                            otherObjects.get(position).setDescription(desc);
                            otherObjects.get(position).setPassword(password);
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
        ImageButton deleteButton;
        ImageButton editButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            idText=(TextView)itemView.findViewById(R.id.idText);
            passwordText=(TextView)itemView.findViewById(R.id.passwordText);
            deleteButton=(ImageButton) itemView.findViewById(R.id.deleteButton);
            editButton=(ImageButton) itemView.findViewById(R.id.editButton);
        }
    }
}
