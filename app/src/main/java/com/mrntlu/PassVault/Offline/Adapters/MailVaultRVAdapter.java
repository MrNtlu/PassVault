package com.mrntlu.PassVault.Offline.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.mrntlu.PassVault.Offline.ClassController;
import com.mrntlu.PassVault.Offline.Models.MailObject;
import com.mrntlu.PassVault.R;
import java.io.FileOutputStream;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class MailVaultRVAdapter extends RecyclerView.Adapter<MailVaultRVAdapter.MyViewHolder> {

    Context context;
    Dialog customDialog;

    FileOutputStream fos=null;
    FileOutputStream fosPass=null;

    private RealmResults<MailObject> mailObjects;
    private ArrayList<Boolean> passBool;
    ClassController classController;
    Realm realm;

    private boolean isSearching=false;

    public MailVaultRVAdapter(Context context, final RealmResults<MailObject> mailObjects, final ArrayList<Boolean> passBool) {
        this.context = context;
        this.mailObjects=mailObjects;
        this.passBool=passBool;
        classController=new ClassController(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_mail_cell,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);
        realm=Realm.getDefaultInstance();
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.idText.setText(mailObjects.get(position).getMail());
        classController.setColorImage(holder.idText.getText().toString(),holder.img);

        if (isSearching) {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
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


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog=new Dialog(context);
                showPopup(v,position);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are You Sure?");
                builder.setMessage("Do you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                try {
                                    passBool.remove(position);
                                    mailObjects.get(position).deleteFromRealm();
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                    Toasty.error(context,e.getMessage(),Toast.LENGTH_SHORT).show();
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
        return mailObjects.size();
    }

    public void showPopup(View v,final int position){
        Button editAdd,editClose;
        final TextView editID,editPassword;

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
        ImageButton deleteButton;
        ImageButton editButton;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            idText=(TextView)itemView.findViewById(R.id.idText);
            passwordText=(TextView)itemView.findViewById(R.id.passwordText);
            deleteButton=(ImageButton) itemView.findViewById(R.id.deleteButton);
            editButton=(ImageButton) itemView.findViewById(R.id.editButton);
            img=itemView.findViewById(R.id.imageColor);
        }
    }
}
