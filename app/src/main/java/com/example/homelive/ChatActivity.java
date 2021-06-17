package com.example.homelive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.homelive.model.Advert;
import com.example.homelive.model.Conversation;
import com.example.homelive.model.Message;
import com.example.homelive.recycler.AdapterMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterMessage adapterMessage;
    private List<Message> messages;
    private FirebaseAuth fbAuth;
    private DatabaseReference databaseReference;

    public static int PICK_IMAGE = 256;
    private TextInputEditText messagetext;
    private ImageButton sendpic;
    private TextInputLayout messagebox;
    private StorageReference storageReference;

    private Advert advertc;
    private Uri messageuri;
    private String uuid;
    private String user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        viewinit();
        recyclermessage();

        //advertc = (Advert) getIntent().getSerializableExtra("advertchat");
        uuid = getIntent().getStringExtra("convuuid");
        databaseReference.child("Users").child(fbAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                user_name = snapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        messagebox.setEndIconOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                newmessage();
            }
        });

        sendpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepic();
            }
        });

        getMessages();


    }


    private void viewinit() {
        messagetext = findViewById(R.id.chat_text);
        messagebox = findViewById(R.id.chat_input);
        sendpic = findViewById(R.id.chat_img);

        fbAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    private void recyclermessage() {

        recyclerView = findViewById(R.id.rv_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messages = new ArrayList<>();
        adapterMessage = new AdapterMessage(messages);

        recyclerView.setAdapter(adapterMessage);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void newmessage() {
        String messuuid = UUID.randomUUID().toString();
        String text = messagetext.getText().toString().trim();
        Message message = new Message();
        if (text.isEmpty()) {

        } else {
            message.setId(messuuid);
            message.setContent(text);
            message.setAuthor(user_name);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            dateFormat.format(date);
            message.setDate(date);
            messages.add(message);
            databaseReference.child("Message").child(uuid).child(messuuid).setValue(message);
            getMessages();
            adapterMessage.notifyDataSetChanged();
            cleanedit();
        }
    }

    private void takepic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    private void dateord() {
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            messageuri = data.getData();
            adapterMessage.notifyDataSetChanged();
            getuserphoto();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getuserphoto() {
        String uid = UUID.randomUUID().toString();
        StorageReference imageReference = storageReference.child("Messages").child(uuid).child(uid).child("message");
        imageReference.putFile(messageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Message m = new Message();
                        String urlphoto = uri.toString();
                        m.setImage(urlphoto);
                        m.setId(uid);
                        m.setAuthor(user_name);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        dateFormat.format(date);
                        m.setDate(date);
                        databaseReference.child("Users").child(fbAuth.getUid()).child("picture").setValue(urlphoto);
                        databaseReference.child("Message").child(uuid).child(uid).setValue(m);
                    }
                });
            }
        });

    }


    private void getMessages() {

        databaseReference.child("Message").child(uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Message m = ds.getValue(Message.class);
                    messages.add(m);
                }
                dateord();
                scrolltoposition();
                adapterMessage.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void scrolltoposition(){
        recyclerView.scrollToPosition(adapterMessage.getItemCount() - 1);
    }

    private void cleanedit() {
        messagetext.setText("");
    }
}