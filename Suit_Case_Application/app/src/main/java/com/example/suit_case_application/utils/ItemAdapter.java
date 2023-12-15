package com.example.suit_case_application.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suit_case_application.R;
import com.example.suit_case_application.model.ItemDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.AdapterViewHolder> {
    private List<ItemDetail> items;
    private Context context;
    private OnClick itm;

    public ItemAdapter(Context c, List<ItemDetail> p, OnClick itm) {
        context = c;
        items = p;
        this.itm = itm;

    }


    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, final int position) {
        Picasso.get().load(items.get(position).getItemImage()).into(holder.itemImage);
        holder.itemNam.setText(items.get(position).getItemName());
        holder.itemDate.setText(items.get(position).getItemDate());
        holder.itemDescrip.setText(items.get(position).getItemDescription());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;

        TextView itemNam, itemDescrip, itemDate;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.journalImage);
            itemNam = itemView.findViewById(R.id.journeyName);
            itemDate = itemView.findViewById(R.id.journeyDate);
            itemDescrip = itemView.findViewById(R.id.journeyDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itm.onItemClicked(getAdapterPosition());
                }
            });

        }
    }
}
