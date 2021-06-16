package com.example.homelive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.homelive.model.Conversation;
import com.example.homelive.recycler.AdapterConversation;
import com.example.homelive.recycler.OnConversationClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConversActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Conversation> conversations;
    private AdapterConversation adapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convers);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fbAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.cv_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        conversations = new ArrayList<>();
        getData();
        //Data
        adapter = new AdapterConversation(conversations, new OnConversationClickListener() {
            @Override
            public void onClick(Conversation conversation) {
                Intent intent = new Intent(ConversActivity.this, ChatActivity.class);
                intent.putExtra("convuuid", conversation.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void getData(){
        databaseReference.child("Users").child(fbAuth.getUid()).child("Conversation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                 conversations.clear();
                 for(DataSnapshot ds: snapshot.getChildren()) {
                     Conversation conv = ds.getValue(Conversation.class);
                     conversations.add(conv);
                 }
                 adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });
    }


}