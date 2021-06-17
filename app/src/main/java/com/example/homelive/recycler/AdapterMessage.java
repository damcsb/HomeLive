package com.example.homelive.recycler;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelive.R;
import com.example.homelive.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.AdapterViewHolder> {

    List<Message> messages;

    public AdapterMessage(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_message, parent, false);
        AdapterViewHolder holder = new AdapterViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView msgtext, msgusername;
        CardView msgbackground;
        ImageView msgimg;

        public AdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            msgtext = itemView.findViewById(R.id.msg_textview);
            msgusername = itemView.findViewById(R.id.msg_user);
            msgimg = itemView.findViewById(R.id.msg_image);
            msgbackground = itemView.findViewById(R.id.msgback);
        }

        public void bind(Message item) {

            msgtext.setText(item.getContent());
            msgusername.setText(item.getAuthor());


            if (item.getImage() != null) {
                msgimg.setVisibility(View.VISIBLE);
                Picasso.get().load(item.getImage()).into(msgimg);
            } else {
                msgimg.setVisibility(View.GONE);
            }



        }

    }
}
