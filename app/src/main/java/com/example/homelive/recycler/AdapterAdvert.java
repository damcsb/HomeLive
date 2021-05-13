package com.example.homelive.recycler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelive.AdvertActivity;
import com.example.homelive.HomeActivity;
import com.example.homelive.R;
import com.example.homelive.SettingsActivity;
import com.example.homelive.model.Advert;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AdapterAdvert extends RecyclerView.Adapter<AdapterAdvert.AdapterViewHolder> {

    List<Advert> adverts;
    private OnAdvertClickInfo listener;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;

    public AdapterAdvert(List<Advert> adverts, OnAdvertClickInfo listener) {
        this.adverts = adverts;
        this.listener = listener;
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
        ImageView userpic;
        TextView email, phone, title, description, price, city;
        ImageButton info;


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
            info = itemView.findViewById(R.id.hv_info);
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

           mStorage = FirebaseStorage.getInstance();
           storageReference = mStorage.getReference();

            storageReference.child("Adverts").child(item.getUid()).child("0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(picture);
                }
            });

           info.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   listener.onClick(item);
               }
           });
        }

    }
}
