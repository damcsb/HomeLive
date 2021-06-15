package com.example.homelive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homelive.model.Advert;
import com.example.homelive.model.Conversation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {

    private ImageView advertpic;
    private CircleImageView profilepic;
    private TextView advertauth, advertdesc, advertplace;
    private FloatingActionButton floatingActionButton;
    private StorageReference storageReference;
    private FirebaseAuth fbAuth;
    private DatabaseReference databaseReference;

    private ArrayList<Uri> imagesUri;
    Advert advertinf;
    String user_img;
    String user_username;
    String passuuid;
    private boolean c = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ViewInits();

        advertinf = (Advert) getIntent().getSerializableExtra("adinf");
        getData();

        storageReference = FirebaseStorage.getInstance().getReference();

        advertauth.setText(advertinf.getEmail());
        advertdesc.setText(advertinf.getDescription());
        advertplace.setText(advertinf.getCity());


        Uri authorpic = Uri.parse(advertinf.getUserpic());
        Picasso.get().load(authorpic).into(profilepic);


        StorageReference imagesReference = storageReference.child(advertinf.getUid());
        Task<ListResult> listResultTask = imagesReference.listAll();
        listResultTask.addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<ListResult> task) {
                List<StorageReference> items = task.getResult().getItems();
                for(int i = 0; i < items.size(); i++){

                    //imagesUri.add(items.get(i));
                }
            }
        });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobationconversation();
            }
        });

    }

    private void createconversation(){

        passuuid = UUID.randomUUID().toString();

        Conversation conversation1 = new Conversation();
        conversation1.setId(passuuid);
        conversation1.setUsername(advertinf.getUsername());
        conversation1.setImg(advertinf.getUserpic());

        Conversation conversation2 = new Conversation();
        conversation2.setId(passuuid);
        conversation2.setUsername(user_username);
        conversation2.setImg(user_img);

        DatabaseReference Convref = databaseReference.child("Users").child(fbAuth.getUid()).child("Conversation").child(advertinf.getUserid());
        DatabaseReference Convrefother = databaseReference.child("Users").child(advertinf.getUserid()).child("Conversation").child(fbAuth.getUid());

        Convref.setValue(conversation1);
        Convrefother.setValue(conversation2);

    }

    private void comprobationconversation(){
        String uid = fbAuth.getUid();
        databaseReference.child("Users").child(fbAuth.getUid()).child("Conversation").child(advertinf.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(getApplicationContext(), "You have a conversation with this user", Toast.LENGTH_LONG).show();
                }else if(advertinf.getUserid() == uid){
                    Toast.makeText(InfoActivity.this, "You cant create a conversation with yourself", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    AlertConv();
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });
    }




    private void ViewInits(){
        advertpic = findViewById(R.id.info_image);
        profilepic = findViewById(R.id.info_propic);
        advertauth = findViewById(R.id.info_username);
        advertdesc = findViewById(R.id.info_desc);
        advertplace = findViewById(R.id.info_place);
        floatingActionButton = findViewById(R.id.info_floatbtn);
        fbAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    private void getData(){
        databaseReference.child("Users").child(fbAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                user_img = snapshot.child("picture").getValue().toString();
                user_username = snapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });
    }

    private void AlertConv(){
        MaterialAlertDialogBuilder Dialog = new MaterialAlertDialogBuilder(InfoActivity.this);
        Dialog.setTitle("Create Conversation");
        Dialog.setMessage("Do you want to open a conversation with "+advertinf.getUsername()+"?");
        Dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createconversation();
                Intent intent = new Intent(InfoActivity.this, ChatActivity.class);
                intent.putExtra("advertchat", advertinf);
                intent.putExtra("convuuid", passuuid);
                startActivity(intent);

            }
        });
        Dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).show();
    }

    private void checkconv(){

    }
}