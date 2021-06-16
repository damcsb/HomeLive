package com.example.homelive.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelive.R;
import com.example.homelive.model.Conversation;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterConversation extends RecyclerView.Adapter<AdapterConversation.AdapterViewHolder> {

    private List<Conversation> conversations;
    public static OnConversationClickListener listener;

    public AdapterConversation(List<Conversation> conversations, OnConversationClickListener listener) {
        this.conversations = conversations;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conver_rv, parent, false);
        AdapterViewHolder holder = new AdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterViewHolder holder, int position) {
        holder.bind(conversations.get(position));
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        CardView container;
        ImageView userpic;
        TextView textView;

        public AdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userpic = itemView.findViewById(R.id.conver_img);
            textView = itemView.findViewById(R.id.conver_text);
            container = itemView.findViewById(R.id.conver_container);
        }

        public void bind(Conversation item) {
            textView.setText(item.getUsername());
            Picasso.get().load(item.getImg()).into(userpic);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });

        }
    }
}
