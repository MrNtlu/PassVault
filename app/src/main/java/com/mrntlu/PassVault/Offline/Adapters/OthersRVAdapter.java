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
import com.mrntlu.PassVault.Offline.Models.OthersObject;
import com.mrntlu.PassVault.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class OthersRVAdapter extends RecyclerView.Adapter<OthersRVAdapter.MyViewHolder> {

    private Context context;
    private Dialog customDialog;

    private RealmResults<OthersObject> otherObjects;
    private ArrayList<Boolean> passBool;
    private ClassController classController;
    private Realm realm;
    private FragmentActivity fragmentActivity;
    private OthersRVAdapter adapter;

    private boolean isSearching=false;

    public OthersRVAdapter(Context context, final RealmResults<OthersObject> otherObjects, final ArrayList<Boolean> passBool,Realm realm, FragmentActivity fragmentActivity) {
        this.context = context;
        this.otherObjects=otherObjects;
        this.passBool = passBool;
        this.realm=realm;
        this.fragmentActivity=fragmentActivity;
        classController=new ClassController(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_other_cell,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.idText.setText(otherObjects.get(position).getDescription());

        classController.setColorImage(holder.idText.getText().toString(),holder.img);

        if (isSearching) {
            holder.menuButton.setVisibility(View.GONE);
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
                                classController.adapterDeleteButton(realm,passBool,otherObjects.get(position),position,adapter);
                                return true;
                            case R.id.moveMenuButton:
                                classController.adapterMoveOnlineButton(fragmentActivity,"Please Edit Title",otherObjects.get(position).getDescription(),otherObjects.get(position).getPassword());
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
        adapter=(OthersRVAdapter)recyclerView.getAdapter();
    }

    @Override
    public int getItemCount() {
        return otherObjects.size();
    }

    public void showPopup(final int position){
        Button editAdd,editClose;
        final TextView editDesc,editPassword;
        if (Build.VERSION.SDK_INT==21){
            customDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
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
        ImageButton menuButton;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            idText=(TextView)itemView.findViewById(R.id.idText);
            passwordText=(TextView)itemView.findViewById(R.id.passwordText);
            menuButton =(ImageButton) itemView.findViewById(R.id.menuButton);
            img=itemView.findViewById(R.id.imageColor);
        }
    }
}
