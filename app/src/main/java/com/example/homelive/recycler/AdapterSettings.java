package com.example.homelive.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelive.R;
import com.example.homelive.model.Advert;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSettings extends RecyclerView.Adapter<AdapterSettings.AdapterViewHolder> {

    List<Advert> adverts;
    public static OnAdvertClickListener listener;

    public AdapterSettings(List<Advert> adverts, OnAdvertClickListener listener) {
        this.adverts = adverts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_rv, parent, false);
        AdapterViewHolder holder = new AdapterViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        holder.bind(adverts.get(position));
    }

    @Override
    public int getItemCount() {
        return adverts.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        CircleImageView adpicture;
        TextView adtitle, addate, adprice;
        Button btn_del, btn_edit;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            adpicture = itemView.findViewById(R.id.rvs_image);
            adtitle = itemView.findViewById(R.id.rvs_title);
            addate = itemView.findViewById(R.id.rvs_date);
            adprice = itemView.findViewById(R.id.rvs_price);
            btn_del = itemView.findViewById(R.id.rvs_del);
            btn_edit = itemView.findViewById(R.id.rvs_edit);
        }
        public void bind(Advert item){
            adtitle.setText(item.getTittle());
            adprice.setText(String.valueOf(item.getPrice()) + "€");
            addate.setText(item.getDate());

            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(item, OnAdvertClickListener.DEL_AD);
                }
            });
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(item, OnAdvertClickListener.EDIT_AD);
                }
            });

        }
    }
}
