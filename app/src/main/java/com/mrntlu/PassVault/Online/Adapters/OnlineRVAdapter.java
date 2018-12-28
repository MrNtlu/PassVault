package com.mrntlu.PassVault.Online.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mrntlu.PassVault.Online.OnlineDialog;
import com.mrntlu.PassVault.Online.Viewmodels.OnlineViewModel;
import com.mrntlu.PassVault.R;
import com.parse.ParseObject;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class OnlineRVAdapter extends RecyclerView.Adapter<OnlineRVAdapter.MyViewHolder> {

    List<ParseObject> parseObjects;
    Context context;
    private OnlineDialog dialogClass;
    private Boolean isSearching=false;

    public OnlineRVAdapter(List<ParseObject> parseObjects, Context context, OnlineViewModel viewModel) {
        this.parseObjects = parseObjects;
        this.context = context;
        dialogClass=new OnlineDialog(parseObjects,context,viewModel);
    }

    public OnlineRVAdapter(List<ParseObject> parseObjects, Context context, OnlineViewModel viewModel,Boolean isSearching) {
        this.parseObjects = parseObjects;
        this.context = context;
        this.isSearching=isSearching;
        dialogClass=new OnlineDialog(parseObjects,context,viewModel);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.custom_online_cell,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.titleText.setText(parseObjects.get(position).getString("Title"));
        holder.usernameText.setText(parseObjects.get(position).getString("Username"));
        holder.passwordHolder.setText(parseObjects.get(position).getString("Password"));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(holder.titleText.getText().toString().trim().substring(0, 1), color);

        holder.imageColor.setImageDrawable(drawable);

        holder.selectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(holder.passwordHolder.getText().toString(), holder.passwordHolder.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toasty.success(context, "Password Copied!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toasty.error(context, "Error! Please try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        if (!isSearching) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogClass.showDialog(position, holder.titleText.getText().toString(), holder.usernameText.getText().toString(), holder.passwordHolder.getText().toString());
                }
            });
        }
        else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogClass.searchDialog(position, holder.titleText.getText().toString(), holder.usernameText.getText().toString(), holder.passwordHolder.getText().toString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageColor;
        ImageButton selectionButton;
        TextView titleText,usernameText,passwordHolder;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageColor=(ImageView) itemView.findViewById(R.id.imageColor);
            selectionButton=(ImageButton)itemView.findViewById(R.id.selectionButton);
            titleText=(TextView)itemView.findViewById(R.id.titleText);
            usernameText=(TextView)itemView.findViewById(R.id.usernameText);
            passwordHolder=(TextView)itemView.findViewById(R.id.passwordHolder);
        }
    }
}
