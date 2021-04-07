package com.example.homelive.recycler;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelive.R;
import com.example.homelive.model.Advert;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAdvert extends RecyclerView.Adapter<AdapterAdvert.AdapterViewHolder> {

    List<Advert> adverts;

    public AdapterAdvert(List<Advert> adverts) {
        this.adverts = adverts;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_rv, parent, false);
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

        ImageView picture;
        CircleImageView userpic;
        TextView email, phone, title, description, price, city;
        MaterialButton chat;


        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.hv_img);
            userpic = itemView.findViewById(R.id.hv_userpic);
            email = itemView.findViewById(R.id.hv_email);
            phone = itemView.findViewById(R.id.hv_phone);
            title = itemView.findViewById(R.id.hv_title);
            description = itemView.findViewById(R.id.hv_desc);
            price = itemView.findViewById(R.id.hv_price);
            city = itemView.findViewById(R.id.hv_city);
            chat = itemView.findViewById(R.id.hv_contact);
        }

        public void bind(Advert item){
           email.setText(item.getEmail());
           phone.setText(String.valueOf(item.getPhone()));
           title.setText(item.getTittle());
           description.setText(item.getDescription());
           price.setText(String.valueOf(item.getPrice()) + "€");
           city.setText(item.getCity());
           Uri authorpic = Uri.parse(item.getUserpic());
           Picasso.get().load(authorpic).into(userpic);
        }
    }
}
