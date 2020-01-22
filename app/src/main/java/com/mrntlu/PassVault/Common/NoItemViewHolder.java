package com.mrntlu.PassVault.Common;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrntlu.PassVault.R;

public class NoItemViewHolder extends RecyclerView.ViewHolder {
    public TextView noneText;
    public ImageView noneImage;
    public NoItemViewHolder(@NonNull View itemView) {
        super(itemView);
        noneText=itemView.findViewById(R.id.noneText);
        noneImage=itemView.findViewById(R.id.noneImage);
    }
}
